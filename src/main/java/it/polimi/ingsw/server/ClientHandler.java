package it.polimi.ingsw.server;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.rmi.AlreadyBoundException;

public abstract class ClientHandler extends Thread {
    final int id;
    private final MainServer server;
    String username;
    private boolean connected;
    private GameState state;

    public ClientHandler() {
        this.server = null;
        this.id = -1;
        this.connected = false;
    }

    public ClientHandler(MainServer server, int id) throws IOException, AlreadyBoundException {
        this.server = server;
        this.id = id;
        this.state = GameState.LOGIN;
        this.username = String.valueOf(id);
    }

    public synchronized void run() {
        Message response;
        try {
            //Ask the client for username and password.
            Message message = new Message(MessageType.LOGIN_REQUEST);
            send(message);

            //start listening for requests from client
            while (state != GameState.CLOSE) {
                message = read();

                if (message.getType() == MessageType.STATE_UPD) {
                    this.state = ((StateUpdate) message).newState;
                    Logger.info("Stato di " + username + '(' + username + ") aggiornato in " + ((StateUpdate) message).newState); //TODO togli
                } else {
                    switch (this.state) {
                        case LOGIN -> {
                            if (message.getType() == MessageType.LOGIN_RESPONSE) {
                                LoginResponse line = (LoginResponse) message;
                                Logger.debug("Username chosen: " + line.getUsername());
                                Logger.debug("Password chosen: " + line.getPassword());
                                //TODO togli

                                if (server.setCredentials(line.getUsername(), line.getPassword(), this)) {
                                    this.username = line.getUsername();
                                    response = new Message(MessageType.LOGIN_SUCCESS);
                                    response.setAuthor(username);
                                    send(response);
                                    if (server.lobbies.isActive(line.getUsername()) != -1) {
                                        server.lobbies.get(server.lobbies.isActive(line.getUsername())).switchHandler(this, line.getUsername());
                                    }
                                } else {
                                    response = new Message(MessageType.LOGIN_FAILURE);
                                    send(response);
                                }

                            } else {
                                Logger.warning("Message " + message.getType().toString() + " received by " + username + "(" + username + ") not accepted in " + this.state.toString());
                            }
                        }
                        case CREATE_JOIN -> {
                            switch (message.getType()) {
                                case CREATE -> {
                                    CreateMessage lobbyInfo = (CreateMessage) message;
                                    int lobbyId = server.lobbies.createLobby(server.getUser(username), lobbyInfo.lobbyDim);
                                    server.getUser(username).setLobbyId(lobbyId);
                                    server.getLobby(lobbyId).openChat(username);
                                    response = new Message(MessageType.JOIN_SUCCEED);
                                    send(response);
                                    response = new LobbiesList(server.lobbies.lobbiesData(), true);
                                    server.sendAll(response);
                                    response = new LobbyData(lobbyId, server.getLobby(lobbyId).getUsers());
                                    send(response);
                                }
                                case JOIN -> {
                                    response = new LobbiesList(server.lobbies.lobbiesData(), false);
                                    send(response);
                                }
                                case EXIT_LOBBY -> exit_lobby();
                                default ->
                                        Logger.warning("Message " + message.getType().toString() + " received by " + username + "(" + username + ") not accepted in " + this.state.toString());
                            }
                        }
                        case LOBBY_CHOICE -> {
                            if (message.getType() == MessageType.JOIN_LOBBY) {
                                boolean added = false;
                                Lobby lobby = server.getLobby(message.lobbyId);

                                if (server.lobbies.contains(message.lobbyId)) {
                                    added = lobby.addUser(server.getUser(message.getAuthor()));
                                }
                                if (!server.lobbies.contains(message.lobbyId) || !added) {
                                    response = new Message(MessageType.JOIN_FAILURE);
                                    send(response);
                                } else {
                                    server.getLobby(message.lobbyId).openChat(username);
                                    response = new Message(MessageType.JOIN_SUCCEED);
                                    //this.lobbyId = message.lobbyId;
                                    send(response);
                                }
                                int lobbyId = server.getLobby(username).id;
                                server.sendToLobby(lobby.id, new LobbyData(lobbyId, lobby.getUsers()));

                                response = new LobbiesList(server.lobbies.lobbiesData(), true);
                                server.sendAll(response);
                            } else {
                                Logger.warning("Message " + message.getType().toString() + " received by " + username + "(" + username + ") not accepted!");
                            }
                        }
                        case INSIDE_LOBBY -> {
                            switch (message.getType()) {
                                case START -> {
                                    int id = server.getUser(username).getLobbyId();
                                    //checks if the user is in a lobby, if it's the admin of the lobby and if the lobby has enough players to start a game.
                                    if (id != -1 && server.getLobby(id).getUsers()[0].equals(username) && server.getLobby(id).getUsers().length <= 4 && server.getLobby(id).getUsers().length >= 2) {
                                        state = GameState.IN_GAME;
                                        server.getLobby(id).resetChat();
                                        server.getLobby(id).startGame();
                                    } else {
                                        send(new StringRequest("Not enough players to start the game"));
                                    }
                                }
                                case EXIT_LOBBY -> exit_lobby();
                                case CHAT_MESSAGE -> {
                                    int id = server.getUser(username).getLobbyId();
                                    server.getLobby(id).updateChat(((ChatMessage) message));
                                }
                                default ->
                                        Logger.warning("Message " + message.getType().toString() + " received by " + username + "(" + username + ") not accepted!");
                            }
                        }
                        case IN_GAME -> {
                            switch (message.getType()) {
                                case CHAT_MESSAGE -> {
                                    int id = server.getUser(username).getLobbyId();
                                    server.getLobby(id).updateChat(((ChatMessage) message));
                                }
                                case TILES_RESPONSE -> {
                                    int lobbyId = server.getLobby(username).id;
                                    if (server.getLobby(username).getCurrPlayer().equals(username)) {
                                        server.getLobby(lobbyId).onTileReceived(((TilesResponse) message).getSelectedTiles());
                                    }
                                }
                                case COLUMN_RESPONSE -> {
                                    int lobbyId = server.getLobby(username).id;
                                    if (server.getLobby(lobbyId).getCurrPlayer().equals(username)) {
                                        server.getLobby(lobbyId).onColumnReceived(((ColumnResponse) message).selectedColumn);
                                    }
                                }
                                default ->
                                        Logger.warning("Message " + message.getType().toString() + " received by " + username + "(" + username + ") not accepted in " + this.state.toString());
                            }
                        }
                        default ->
                                Logger.warning("Message " + message.getType().toString() + " received by " + username + "(" + username + ") not accepted!");
                    }
                }
            }
            disconnect();

        } catch (IOException | ClassNotFoundException e) {
            Logger.error("An error occurred on thread " + id + " while waiting for connection or with write method.");
            disconnect();
            //remove the client form the lobby if already in one

            Logger.debug(username + " disconnected");
        }
    }

    abstract String getAddress();

    boolean isConnected() {
        return connected;
    }

    abstract void send(Message m) throws IOException;

    int GetId() {
        return id;
    }

    boolean equals(ClientHandler other) {
        return id == other.GetId();
    }

    public void setGameState(GameState state) {
        this.state = state;
    }

    private void exit_lobby() {
        int lobbyId = server.getUser(username).getLobbyId();
        if (lobbyId != -1) {
            server.lobbies.removeUser(username);
            try {
                server.sendAll(new LobbiesList(server.lobbies.lobbiesData(), true));
                if (server.lobbies.get(lobbyId) != null) {
                    server.sendToLobby(lobbyId, new LobbyData(lobbyId, server.lobbies.get(lobbyId).getUsers()));
                }
            } catch (IOException i) {
                throw new RuntimeException();
            }
        }
    }

    void disconnect() {
        connected = false;
        int lobbyId = server.getUser(username).getLobbyId();
        if (lobbyId != -1) {
            if (state != GameState.IN_GAME) {
                exit_lobby();
            } else {
                if (server.getLobby(lobbyId).nConnectedUsers() == 0) {
                    server.lobbies.removeLobby(lobbyId);
                }
                server.getLobby(lobbyId).skip(username);
            }
        }
    }

    abstract Message read() throws IOException, ClassNotFoundException;

    public void connect() {
        connected = true;
    }
}