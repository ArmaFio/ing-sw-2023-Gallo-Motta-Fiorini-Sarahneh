package it.polimi.ingsw.server;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.utils.LoadSave;
import it.polimi.ingsw.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientHandler extends Thread {

    private final int id;
    private final String userAddress;
    private final MainServer server;
    @Deprecated
    private final Object syn = new Object();
    private GameState state;
    private String username;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    @Deprecated
    private Scanner sc;
    @Deprecated
    private File accounts;
    @Deprecated
    private HashMap<String, String> usersPassword;
    private int lobbyId;


    /**
     * ClientHandler constructor, handles the connection with the client until a new game is created or the client decides to join a game.
     *
     * @param id       thread id, only visible in the server.
     * @param listener client socket.
     */
    public ClientHandler(MainServer server, int id, Socket listener) throws IOException {
        this.server = server;
        this.id = id;
        this.userAddress = listener.getRemoteSocketAddress().toString();
        this.state = GameState.LOGIN;
        this.outputStream = new ObjectOutputStream(listener.getOutputStream());
        this.inputStream = new ObjectInputStream(listener.getInputStream());
        Logger.info("The thread " + id + " is now connected with the player ip " + userAddress);
        this.start();
    }

    @Override
    public void run() {
        Message response;
        Lobby lobby;
        try {
            //Ask the client for username and password.
            Message message = new Message(MessageType.LOGIN_REQUEST); //TODO serve?
            send(message);

            //start listening for requests from client
            while (state != GameState.CLOSE) {
                message = read();
                //TODO if user == message.author;
                if (message.getType() == MessageType.UPD_STATE) {
                    this.state = ((UpdateState) message).newState;
                    Logger.info("Stato aggiornato in " + ((UpdateState) message).newState);
                } else {
                    switch (this.state) {
                        case LOGIN -> {
                            if (message.getType() == MessageType.LOGIN_RESPONSE) {
                                LoginResponse line = (LoginResponse) message;
                                Logger.debug("Username chosen: " + line.getAuthor());
                                Logger.debug("Password chosen: " + line.getPassword());
                                //TODO gli account sono memorizzati correttamente ma se il server crasha si persono le informazioni in users, rimangono solo le coppie username-password
                                if (!server.users.contains(line.getAuthor())) {
                                    Logger.debug("Adding username");
                                    server.users.add(new User(line.getAuthor(), line.getPassword(), this));
                                    this.username = line.getAuthor();
                                    //response = new StringRequest("Creating a new account...", "Server"); non serve, c'è LOGIN_SUCCESS/LOGIN_FAILURE
                                    LoadSave.write(MainServer.PASSWORDS_PATH, server.users.getPasswordsMap());
                                    response = new Message(MessageType.LOGIN_SUCCESS);
                                } else if (server.users.contains(line.getAuthor()) && server.getUser(line.getAuthor()).checkPassword(line.getPassword()) && !server.getUser(line.getAuthor()).isConnected()) {
                                    this.username = line.getAuthor();
                                    server.getUser(line.getAuthor()).setClient(this);
                                    server.getUser(line.getAuthor()).setConnected(true);
                                    LoadSave.write(MainServer.PASSWORDS_PATH, server.users.getPasswordsMap());
                                    response = new Message(MessageType.LOGIN_SUCCESS);
                                } else {
                                    response = new Message(MessageType.LOGIN_FAILURE);
                                }

                                send(response);
                            } else {
                                Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted in " + this.state.toString());
                            }
                        }
                        case CREATE_JOIN -> {
                            switch (message.getType()) {
                                case CREATE -> {
                                    int lobbyId = server.lobbies.createLobby(server.getUser(username));
                                    server.getUser(username).setLobbyId(lobbyId);
                                    Lobby newLobby = server.getLobby(lobbyId);
                                    response = new JoinSuccess(newLobby.id, newLobby.getUsers());
                                    Logger.debug("Lobby " + newLobby.id + " created");

                                    send(response);

                                    response = new LobbyList(server.lobbies.lobbiesData());//TODO notifylobbyUpdate()
                                    server.sendAll(response);
                                }
                                case JOIN -> {
                                    //TODO va cambiato, è necessario avere anche la lista di tutti gli utenti all'interno delle varie lobby.
                                    response = new LobbyList(server.lobbies.lobbiesData());
                                    send(response);
                                }
                                default ->
                                        Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted in " + this.state.toString());
                            }
                        }
                        case LOBBY_CHOICE -> {
                            if (message.getType() == MessageType.JOIN_LOBBY) {
                                boolean added = false;
                                lobby = server.getLobby(message.lobbyId);

                                if (server.lobbies.contains(message.lobbyId)) {
                                    added = lobby.addUser(server.getUser(message.getAuthor()));
                                }
                                if (!server.lobbies.contains(message.lobbyId) || !added) {
                                    response = new Message(MessageType.JOIN_FAILURE); //TODO JOIN_OUTCOME
                                } else {
                                    response = new JoinSuccess(lobby.id, lobby.getUsers());
                                    this.lobbyId = message.lobbyId;
                                }

                                send(response);

                                response = new LobbyList(server.lobbies.lobbiesData());
                                server.sendAll(response);
                            } else {
                                Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted!");
                            }
                        }
                        case INSIDE_LOBBY -> {
                            if (message.getType() == MessageType.START) {
                                int id = server.getUser(username).getLobbyId();
                                //checks if the user is in a lobby, if it's the admin of the lobby and if the lobby has enough players to start a game.
                                if (id != -1 && server.getLobby(id).getUsers()[0].equals(username) && server.getLobby(id).getUsers().length <= 4 && server.getLobby(id).getUsers().length >= 2) {
                                    server.getLobby(id).startGame();
                                    server.sendAll(new Message(MessageType.START));
                                } else {
                                    if (!(server.getLobby(id).getUsers().length <= 4 && server.getLobby(id).getUsers().length >= 2)) {
                                        StringRequest notify = new StringRequest("Not enough players to start a game!", "Server");
                                        send(notify);
                                    } else {
                                        if (!server.getLobby(id).getUsers()[0].equals(username)) {
                                            StringRequest notify = new StringRequest("Game can't be started because the player is not the admin!", "Server");
                                            send(notify);
                                        } else {
                                            StringRequest notify = new StringRequest("Game can't be started because the user is not in a Lobby", "Server");
                                            send(notify);
                                        }
                                    }
                                }
                            } else {
                                Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted!");
                            }
                        }
                        case IN_GAME -> {
                            switch (message.getType()) {
                                case TILES_RESPONSE -> {
                                    if (server.getLobby(lobbyId).getCurrPlayer().equals(username)) {
                                        server.getLobby(lobbyId).onTileReceived(((TilesResponse) message).getSelectedTiles());
                                    }
                                    //TODO controlla se va bene la scelta.
                                }
                                case COLUMN_RESPONSE -> {
                                    if (server.getLobby(lobbyId).getCurrPlayer().equals(username)) {
                                        server.getLobby(lobbyId).onColumnReceived(((ColumnResponse) message).selectedColumn);
                                    }
                                    //TODO controlla se va bene la scelta.
                                }
                                default ->
                                        Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted in " + this.state.toString());
                            }
                        }
                        default -> {
                            Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted!");
                        }
                    }
                }


            }
        } catch (IOException e) {
            Logger.error("An error occurred on thread " + id + " while waiting for connection or with write method.");
            disconnect();
            //remove the client form the lobby if already in one
            int lobbyId = server.getUser(username).getLobbyId();
            if (lobbyId != -1) {
                server.lobbies.removeUser(username);
                Logger.debug(username + " Removed from lobby " + lobbyId);
            }
            Logger.debug(username + " disconnected");
        } catch (ClassNotFoundException i) {
            Logger.error("An error occurred on thread " + id + " while reading the received object.");
        }
    }

    /**
     * Reads a serialized object received from the client.
     *
     * @return the object read.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Message read() throws ClassNotFoundException, IOException {
        Message msg = (Message) inputStream.readObject();
        Logger.info("Message " + msg.getType().toString() + " received by " + userAddress + "(" + username + ")");
        return msg;
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param obj the object we want to send to the client.
     * @throws IOException
     */
    public void send(Message obj) throws IOException {
        obj.setAuthor(String.valueOf(id));
        outputStream.writeObject(obj);
    }

    /**
     * Sets the {@isConnected} state of the user to false.
     */
    private void disconnect(){
        server.getUser(username).setConnected(false);
    }
}
