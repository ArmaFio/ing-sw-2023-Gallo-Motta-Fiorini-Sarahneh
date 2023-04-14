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
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    @Deprecated
    private Scanner sc;
    @Deprecated
    private File accounts;
    @Deprecated
    private HashMap<String, String> usersPassword;


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
        this.start();
        Logger.info("The thread " + id + " is now connected with the player ip " + userAddress);
    }

    @Override
    public void run() {
        Message response;
        Lobby lobby;
        try {
            //Ask the client for username and password.
            Message message = new Message(ResponseType.LOGIN_REQUEST); //TODO serve?
            write(message);

            //start listening for requests from client
            while (state != GameState.CLOSE) {
                message = read();
                //TODO if user == message.author;
                if (message.getType() == ResponseType.UPD_STATE) {
                    this.state = ((UpdateState) message).newState;
                    Logger.info("Stato aggiornato");
                } else {
                    switch (this.state) {
                        case LOGIN -> {
                            switch (message.getType()) {
                                case LOGIN_RESPONSE -> {
                                    LoginResponse line = (LoginResponse) message;
                                    Logger.debug("Username chosen: " + line.getAuthor());
                                    Logger.debug("Password chosen: " + line.getPassword());
                                    server.users.contains(line.getAuthor());
                                    //TODO gli account sono memorizzati correttamente ma se il server crasha si persono le informazioni in users, rimangono solo le coppie username-password
                                    if (!server.users.contains(line.getAuthor())) { //TODO != null constrolla anche che non sia già connesso
                                        Logger.debug("Adding username");
                                        server.users.add(new User(line.getAuthor(), line.getPassword(), this));
                                        this.username = line.getAuthor();
                                        //response = new StringRequest("Creating a new account...", "Server"); non serve, c'è LOGIN_SUCCESS/LOGIN_FAILURE
                                        LoadSave.write(MainServer.PASSWORDS_PATH, server.users.getPasswordsMap());
                                        response = new Message(ResponseType.LOGIN_SUCCESS);
                                    } else if (server.users.contains(line.getAuthor()) && server.users.get(line.getAuthor()).checkPassword(line.getPassword()) && !server.users.get(line.getAuthor()).isConnected()) {
                                        this.username = line.getAuthor();
                                        server.users.get(line.getAuthor()).setClient(this);
                                        server.users.get(line.getAuthor()).setConnected(true);
                                        LoadSave.write(MainServer.PASSWORDS_PATH, server.users.getPasswordsMap());
                                        response = new Message(ResponseType.LOGIN_SUCCESS);
                                    } else {
                                        response = new Message(ResponseType.LOGIN_FAILURE);
                                    }

                                    write(response);
                                }
                                case CREATE -> {
                                    int lobbyId = server.lobbies.createLobby(username);
                                    server.users.get(username).setLobbyId(lobbyId);
                                    Lobby newLobby = server.lobbies.get(lobbyId);
                                    response = new JoinSuccess(newLobby.id, newLobby.getUsers());
                                    Logger.debug("Lobby " + newLobby.id + " created");

                                    write(response);

                                    response = new LobbyList(server.lobbies.lobbiesData());//TODO notifylobbyUpdate()
                                    server.users.sendAll(response);
                                }
                                case JOIN -> {
                                    response = new LobbyList(server.lobbies.lobbiesData());
                                    write(response);
                                }
                                default ->
                                        Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted!");
                            }
                        }
                        case LOBBY_CHOICE -> {
                            if (message.getType() == ResponseType.JOIN_LOBBY) {
                                boolean added = false;
                                lobby = server.lobbies.get(message.lobbyId);

                                if (server.lobbies.contains(message.lobbyId)) {
                                    added = lobby.addUser(server.users.get(message.getAuthor()));
                                }
                                if (!server.lobbies.contains(message.lobbyId) || !added) {
                                    response = new Message(ResponseType.JOIN_FAILURE); //TODO JOIN_OUTCOME
                                } else {
                                    response = new JoinSuccess(lobby.id, lobby.getUsers());
                                }

                                write(response);

                                response = new LobbyList(server.lobbies.lobbiesData());
                                server.users.sendAll(response);
                            } else {
                                Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted!");
                            }
                        }
                        case INSIDE_LOBBY -> {
                            if (message.getType() == ResponseType.START) {
                                if (server.users.get(username).getLobbyId() != -1) {
                                    server.lobbies.get(server.users.get(username).getLobbyId()).startGame();
                                } else {
                                    Logger.warning("Game can't be started because the user is not in a Lobby");
                                }
                            } else {
                                Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted!");
                            }
                        }
                    }
                }


            }
        } catch (IOException e) {
            Logger.error("An error occurred on thread " + id + " while waiting for connection or with write method.");
            disconnect();
            //remove the client form the lobby if already in one
            int lobbyId = server.users.get(username).getLobbyId();
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
    public void write(Message obj) throws IOException {
        obj.setAuthor(String.valueOf(id));
        outputStream.writeObject(obj);
    }

    /**
     * Sets the {@isConnected} state of the user to false.
     */
    private void disconnect(){
        server.users.get(username).setConnected(false);
    }
}
