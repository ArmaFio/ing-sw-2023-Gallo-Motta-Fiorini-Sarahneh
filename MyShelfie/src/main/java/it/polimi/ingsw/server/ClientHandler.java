package it.polimi.ingsw.server;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientHandler extends Thread {
    public final String userAddress;
    final int id;
    private final MainServer server;
    @Deprecated
    private final Object syn = new Object();
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private boolean connected;
    private GameState state;
    private String username;
    @Deprecated
    private Scanner sc;
    @Deprecated
    private File accounts;
    @Deprecated
    private HashMap<String, String> usersPassword;
    private int lobbyId; //TODO forse non serve, basta sapere username


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
        this.username = this.userAddress;
        this.state = GameState.LOGIN;
        this.outputStream = new ObjectOutputStream(listener.getOutputStream());
        this.inputStream = new ObjectInputStream(listener.getInputStream());
        this.connected = true;
        Logger.info("The thread " + id + " is now connected with the player ip " + userAddress);
        this.start();
    }

    public ClientHandler() {
        this.server = null;
        this.id = -1;
        this.userAddress = null;
        this.outputStream = null;
        this.inputStream = null;
        this.connected = false;
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
                if (message.getType() == MessageType.STATE_UPD) {
                    this.state = ((StateUpdate) message).newState;
                    Logger.info("Stato di " + userAddress + '(' + username + ") aggiornato in " + ((StateUpdate) message).newState);
                } else {
                    switch (this.state) {
                        case LOGIN -> {
                            if (message.getType() == MessageType.LOGIN_RESPONSE) {
                                LoginResponse line = (LoginResponse) message;
                                Logger.debug("Username chosen: " + line.getUsername());
                                Logger.debug("Password chosen: " + line.getPassword());
                                //TODO gli account sono memorizzati correttamente ma se il server crasha si persono le informazioni in users, rimangono solo le coppie username-password

                                if (server.setCredentials(line.getUsername(), line.getPassword(), this)) {
                                    this.username = line.getUsername();
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
                                    response = new Message(MessageType.JOIN_SUCCEED);
                                    Logger.debug("Lobby " + newLobby.id + " created");

                                    send(response);

                                    response = new LobbiesList(server.lobbies.lobbiesData(), true);//TODO notifylobbyUpdate()
                                    server.sendAll(response);
                                    response = new LobbyData(lobbyId, server.getLobby(lobbyId).getUsers());
                                    send(response);
                                }
                                case JOIN -> {
                                    //TODO va cambiato, è necessario avere anche la lista di tutti gli utenti all'interno delle varie lobby.
                                    response = new LobbiesList(server.lobbies.lobbiesData(), false);
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
                                    send(response);
                                } else {
                                    response = new Message(MessageType.JOIN_SUCCEED);
                                    this.lobbyId = message.lobbyId;
                                    send(response);
                                }

                                server.sendToLobby(lobby.id, new LobbyData(lobbyId, lobby.getUsers()));

                                response = new LobbiesList(server.lobbies.lobbiesData(), true);
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
                                    server.sendToLobby(id, new Message(MessageType.START));
                                } else {
                                    if (!(server.getLobby(id).getUsers().length <= 4 && server.getLobby(id).getUsers().length >= 2)) {
                                        StringRequest notify = new StringRequest("Not enough players to start a game!");
                                        send(notify);
                                    } else {
                                        if (!server.getLobby(id).getUsers()[0].equals(username)) {
                                            StringRequest notify = new StringRequest("Game can't be started because the player is not the admin!");
                                            send(notify);
                                        } else {
                                            StringRequest notify = new StringRequest("Game can't be started because the user is not in a Lobby");
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
                try {
                    server.sendAll(new LobbiesList(server.lobbies.lobbiesData(), true));
                    server.sendToLobby(lobbyId, new LobbyData(lobbyId, server.lobbies.get(lobbyId).getUsers()));
                } catch (IOException i) {
                    throw new RuntimeException();
                }
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
    private Message read() throws ClassNotFoundException, IOException {
        Message msg = null;

        if (this.connected) {
            msg = (Message) inputStream.readObject();
            Logger.info("Message " + msg.getType().toString() + " received by " + userAddress + "(" + username + ")");
        } else {
            throw new IOException();
        }

        return msg;
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param msg the {@code Message} we want to send to the client.
     * @throws IOException
     */
    public void send(Message msg) throws IOException {
        if (this.connected) {
            msg.setAuthor(this.username);
            this.outputStream.writeObject(msg);
            Logger.info("Message " + msg.getType().toString() + " sent to " + userAddress + "(" + username + ")");
        } else {
            throw new IOException();
        }
    }

    /**
     * Sets the {@isConnected} state of the user to false.
     */
    private void disconnect() {
        connected = false;
    }

    public boolean equals(ClientHandler other) {
        return this.id == other.id;
    }

    public boolean isConnected() {
        return connected;
    }
}
