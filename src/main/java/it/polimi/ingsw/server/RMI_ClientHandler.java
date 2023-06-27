package it.polimi.ingsw.server;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.RMInterface;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class RMI_ClientHandler extends Thread implements ClientHandler {
    final int id;
    private final MainServer server;
    private RMInterfaceSImpl rmi;
    private String userAddress;
    private boolean connected;
    private GameState state;
    private String username;
    private Boolean init;
    private RMInterface client;
    private Object lock = new Object();
    private Timestamp ping;

    public RMI_ClientHandler(MainServer server, int id) throws IOException, AlreadyBoundException {
        this.server = server;
        this.id = id;
        this.state = GameState.LOGIN;
        this.username = String.valueOf(id);
        rmi = new RMInterfaceSImpl(this);
        Logger.info("The thread " + id + " is now connected");
        Registry registry = LocateRegistry.getRegistry(1099);
        registry.bind(id + "RMInterface", rmi);
        Logger.info("RMI Ready");
    }

    public RMI_ClientHandler() {
        this.server = null;
        this.id = -1;
        this.connected = false;
    }

    @Override
    public void run() {
       synchronized (this) {
           try {
               send(new Message(MessageType.LOGIN_REQUEST));
               while (state != GameState.CLOSE) {
                   try {
                       wait();
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }
                   Message message = rmi.getMessage();
                   Logger.info(message.getType() + "Received");
                   if (message.getType() == MessageType.STATE_UPD) {
                       this.state = ((StateUpdate) message).newState;
                       Logger.info("Stato di " + userAddress + '(' + username + ") aggiornato in " + ((StateUpdate) message).newState);
                   } else {
                       switch (this.state) {
                           case LOGIN -> {
                               LoginResponse lr = (LoginResponse) message;
                               if (server.setCredentials(lr.getUsername(), lr.getPassword(), this)) {
                                   if (server.lobbies.isActive(lr.getUsername()) != -1) {
                                       Message m = new Message(MessageType.LOGIN_SUCCESS);
                                       username = lr.getUsername();
                                       m.setAuthor(username);
                                       send(m);
                                       server.lobbies.get(server.lobbies.isActive(lr.getUsername())).switchHandler(this, lr.getUsername());
                                   } else {
                                       Message m = new Message(MessageType.LOGIN_SUCCESS);
                                       username = lr.getUsername();
                                       m.setAuthor(username);
                                       send(m);
                                   }
                               } else {
                                   Message m = new Message(MessageType.LOGIN_FAILURE);
                                   send(m);
                               }
                           }
                           case CREATE_JOIN -> {
                               if (message.getType().equals(MessageType.JOIN)) {
                                   LobbiesList msg = new LobbiesList(server.lobbies.lobbiesData(), false);
                                   send(msg);
                               } else if ((message.getType().equals(MessageType.CREATE))) {
                                   CreateMessage cm = (CreateMessage) message;
                                   int lobbyId = server.lobbies.createLobby(server.getUser(username), cm.lobbyDim);
                                   server.getUser(username).setLobbyId(lobbyId);
                                   Lobby newLobby = server.getLobby(lobbyId);
                                    server.getLobby(lobbyId).openChat(username);
                                   send(new Message(MessageType.JOIN_SUCCEED));
                                   Logger.debug("Lobby " + newLobby.id + " created");


                                   LobbiesList ll = new LobbiesList(server.lobbies.lobbiesData(), true);
                                   try {
                                       server.sendAll(ll);
                                   } catch (IOException e) {
                                       throw new RuntimeException(e);
                                   }
                                   LobbyData ld = new LobbyData(lobbyId, server.getLobby(lobbyId).getUsers());
                                   send(ld);
                               } else if (message.getType().equals((MessageType.EXIT_LOBBY))) {
                                   int lobbyId = server.getUser(username).getLobbyId(); //TODO organizza in una funzione
                                   if (lobbyId != -1) {
                                       server.lobbies.removeUser(username);
                                       Logger.debug(username + " Removed from lobby " + lobbyId);
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
                           }
                           case LOBBY_CHOICE -> {
                               boolean added = false;
                               Lobby l = server.getLobby(message.lobbyId);
                               if (server.lobbies.contains(message.lobbyId))
                                   added = l.addUser(server.getUser(username));
                               if (!server.lobbies.contains(message.lobbyId) || !added) {
                                   send(new Message(MessageType.JOIN_FAILURE));
                               } else {
                                   send(new Message(MessageType.JOIN_SUCCEED));
                                   server.getLobby(message.lobbyId).openChat(username);
                               }
                               int lobbyId = server.getLobby(username).id;
                               try {
                                   server.sendToLobby(l.id, new LobbyData(lobbyId, l.getUsers()));
                               } catch (IOException e) {
                                   throw new RuntimeException(e);
                               }

                               LobbiesList response = new LobbiesList(server.lobbies.lobbiesData(), true);
                               try {
                                   server.sendAll(response);
                               } catch (IOException e) {
                                   throw new RuntimeException(e);
                               }
                           }
                           case INSIDE_LOBBY -> {
                               if (message.getType() == MessageType.START) {
                                   int id = server.getUser(username).getLobbyId();
                                   //checks if the user is in a lobby, if it's the admin of the lobby and if the lobby has enough players to start a game.
                                   if (id != -1 && server.getLobby(id).getUsers()[0].equals(username) && server.getLobby(id).getUsers().length <= 4 && server.getLobby(id).getUsers().length >= 2) {
                                       state = GameState.IN_GAME;
                                       server.getLobby(id).resetChat();
                                       server.getLobby(id).startGame();
                                   } else {
                                       if (!(server.getLobby(id).getUsers().length <= 4 && server.getLobby(id).getUsers().length >= 2)) {
                                           StringMessage notify = new StringMessage("Not enough players to start a game!");
                                           send(notify);
                                       } else {
                                           if (!server.getLobby(id).getUsers()[0].equals(username)) {
                                               StringMessage notify = new StringMessage("Game can't be started because the player is not the admin!");
                                               send(notify);
                                           } else {
                                               StringMessage notify = new StringMessage("Game can't be started because the user is not in a Lobby");
                                               send(notify);
                                           }
                                       }
                                   }
                               } else if (message.getType() == MessageType.EXIT_LOBBY) {
                                   int lobbyId = server.getUser(username).getLobbyId(); //TODO organizza in una funzione
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
                               } else if (message.getType() == MessageType.CHAT_MESSAGE) {
                                   int id = server.getUser(username).getLobbyId();
                                   server.getLobby(id).updateChat((ChatMessage) message);
                               } else {
                                   Logger.warning("Message " + message.getType().toString() + " received by " + userAddress + "(" + username + ") not accepted!");
                               }
                           }
                           case IN_GAME -> {
                               Logger.debug("siamo in game");
                               if (message.getType() == MessageType.CHAT_MESSAGE) {
                                   int id = server.getUser(username).getLobbyId();
                                   server.getLobby(id).updateChat(((ChatMessage) message));
                               } else {
                                   switch (message.getType()) {
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
                                   }
                               }
                           }
                       }
                   }
               }
               disconnect();

           } catch (IOException e) {
               Logger.error("An error occurred on thread " + id + " while waiting for connection or with write method.");
               disconnect();
               //remove the client form the lobby if already in one
               int lobbyId = server.getUser(username).getLobbyId();
               if (lobbyId != -1) {
                   if (state != GameState.IN_GAME) {
                       server.lobbies.removeUser(username);
                       Logger.debug(username + " Removed from lobby " + lobbyId);
                       try {
                           server.sendAll(new LobbiesList(server.lobbies.lobbiesData(), true));
                           server.sendToLobby(lobbyId, new LobbyData(lobbyId, server.lobbies.get(lobbyId).getUsers()));
                       } catch (IOException i) {
                           throw new RuntimeException();
                       }
                   } else
                       server.getLobby(lobbyId).skip(username);
               }
               Logger.debug(username + " disconnected");
           }
       }
    }


    @Override
    public String getAddress() {
        return userAddress;
    }

    public void setAddress(String address) {
        this.userAddress = address;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void send(Message m) throws IOException {
        m.setAuthor(this.username);
        if (this.connected) {
            try {
                client.write(m);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int GetId() {
        return id;
    }

    @Override
    public boolean equals(ClientHandler other) {
        return this.id == other.GetId();
    }

    @Override
    public GameState getGameState() {
        return state;
    }

    @Override
    public void setGameState(GameState state) {
        this.state = state;
    }

    public synchronized void setClient(RMInterface client) throws InterruptedException {
        this.client = client;
        connected = true;
        ping = Timestamp.valueOf(LocalDateTime.now());
        connChecker();
        this.start();
    }


    public synchronized void update() {
        notifyAll();
    }

    public void disconnect() {
        connected = false;
        int lobbyId = server.getUser(username).getLobbyId();
        if (lobbyId != -1) {
            if (server.getLobby(lobbyId).nConnectedUsers() == 0)
                server.lobbies.removeLobby(lobbyId);
        }
    }

    public void connChecker() throws InterruptedException {
        new Thread(() -> {
            Timestamp lastPing = ping;
            int count = 0;
            while (connected) {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (Objects.equals(ping, lastPing)) {
                    if (count < 3)
                        count++;
                    else {
                        Logger.error("An error occurred on thread " + id + " while waiting for connection or with write method.");
                        disconnect();
                        int lobbyId = server.getUser(username).getLobbyId();
                        if (lobbyId != -1) {
                            if (state != GameState.IN_GAME) {
                                server.lobbies.removeUser(username);
                                Logger.debug(username + " Removed from lobby " + lobbyId);
                                try {
                                    server.sendAll(new LobbiesList(server.lobbies.lobbiesData(), true));
                                    server.sendToLobby(lobbyId, new LobbyData(lobbyId, server.lobbies.get(lobbyId).getUsers()));
                                } catch (IOException i) {
                                    throw new RuntimeException();
                                }
                            } else {
                                server.getLobby(lobbyId).skip(username);
                            }
                        }
                        Logger.debug(username + " disconnected");
                    }
                } else {
                    Logger.info("Ping received from " + username);
                    lastPing = ping;
                    count = 0;
                }

            }
        }).start();

    }

    void ping() {
        ping = Timestamp.valueOf(LocalDateTime.now());
    }
}
