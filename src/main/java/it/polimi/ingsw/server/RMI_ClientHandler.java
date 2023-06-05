package it.polimi.ingsw.server;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.RMInterface;
import it.polimi.ingsw.client.RMI_NetworkHandler;
import it.polimi.ingsw.client.RMInterfaceCImpl;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
    private int action;

    public RMI_ClientHandler(MainServer server, int id) throws IOException, AlreadyBoundException {
        this.server = server;
        this.id = id;
        this.state = GameState.LOGIN;
        this.connected = true;
        rmi = new RMInterfaceSImpl(this);
        Logger.info("The thread " + id + " is now connected");
        Registry registry = LocateRegistry.getRegistry(1099);
        registry.bind(id + "RMInterface", rmi);
        Logger.info("RMI Ready");
        this.start();
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
               wait();
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           }
           try {
               send(new Message(MessageType.LOGIN_REQUEST));
               while (state != GameState.CLOSE) {
                   try {
                       wait();
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }
                   if (rmi.getM().getType() == MessageType.STATE_UPD) {
                       this.state = ((StateUpdate) rmi.getM()).newState;
                       Logger.info("Stato di " + userAddress + '(' + username + ") aggiornato in " + ((StateUpdate) rmi.getM()).newState);
                   } else {
                       switch (this.state) {
                           case LOGIN -> {
                               LoginResponse lr = (LoginResponse) rmi.getM();
                               if (server.setCredentials(lr.getUsername(), lr.getPassword(), this)) {
                                   username = lr.getUsername();
                                   Message m = new Message(MessageType.LOGIN_SUCCESS);
                                   send(m);
                               } else {
                                   Message m = new Message(MessageType.LOGIN_FAILURE);
                                   send(m);
                               }
                           }
                           case CREATE_JOIN -> {
                               if (rmi.getM().getType().equals(MessageType.JOIN)) {
                                   LobbiesList msg = new LobbiesList(server.lobbies.lobbiesData(), false);
                                   send(msg);
                               } else if ((rmi.getM().getType().equals(MessageType.CREATE))) {
                                   CreateMessage cm = (CreateMessage) rmi.getM();
                                   int lobbyId = server.lobbies.createLobby(server.getUser(username), cm.lobbyDim);
                                   server.getUser(username).setLobbyId(lobbyId);
                                   Lobby newLobby = server.getLobby(lobbyId);
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
                               }
                           }
                           case LOBBY_CHOICE -> {
                               boolean added = false;
                               Lobby l = server.getLobby(rmi.getM().lobbyId);
                               if (server.lobbies.contains(rmi.getM().lobbyId))
                                   added = l.addUser(server.getUser(username));
                               if (!server.lobbies.contains(rmi.getM().lobbyId) || !added) {
                                   send(new Message(MessageType.JOIN_FAILURE));
                               } else {
                                   send(new Message(MessageType.JOIN_SUCCEED));
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
                               if (rmi.getM().getType() == MessageType.START) {
                                   int id = server.getUser(username).getLobbyId();
                                   //checks if the user is in a lobby, if it's the admin of the lobby and if the lobby has enough players to start a game.
                                   if (id != -1 && server.getLobby(id).getUsers()[0].equals(username) && server.getLobby(id).getUsers().length <= 4 && server.getLobby(id).getUsers().length >= 2) {
                                       state = GameState.IN_GAME;
                                       server.getLobby(id).startGame();
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
                               } else if (rmi.getM().getType() == MessageType.EXIT_LOBBY) {
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
                               } else {
                                   Logger.warning("Message " + rmi.getM().getType().toString() + " received by " + userAddress + "(" + username + ") not accepted!");
                               }
                           }
                           case IN_GAME -> {
                               Logger.debug("siamo in game");
                               switch (rmi.getM().getType()) {
                                   case TILES_RESPONSE -> {
                                       int lobbyId = server.getLobby(username).id;
                                       if (server.getLobby(username).getCurrPlayer().equals(username)) {
                                           server.getLobby(lobbyId).onTileReceived(((TilesResponse) rmi.getM()).getSelectedTiles());
                                       }
                                   }
                                   case COLUMN_RESPONSE -> {
                                       int lobbyId = server.getLobby(username).id;
                                       if (server.getLobby(lobbyId).getCurrPlayer().equals(username)) {
                                           server.getLobby(lobbyId).onColumnReceived(((ColumnResponse) rmi.getM()).selectedColumn);
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
        if (this.connected) {
            new Thread(()-> {
                try {
                    client.write(m);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    @Override
    public int GetId() {
        return id;
    }

    public synchronized void setClient(RMInterface client) {
        this.client = client;
        connected = true;
        notifyAll();
    }


    public synchronized void update() {
        notifyAll();
    }

    public void disconnect() {
        connected = false;
    }
}
