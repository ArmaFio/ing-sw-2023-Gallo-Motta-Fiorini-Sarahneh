package it.polimi.ingsw.server;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Lobby extends Thread {
    public final int id;
    private final ArrayList<User> users;
    public final int lobbyDim;
    private boolean isGameStarted;
    private Controller gameController;
    private boolean isEnded;
    private LobbiesHandler handler;
    private Timestamp lastTimer;

    public Lobby(int id, User admin, int lobbyDim, LobbiesHandler handler) {
        this.id = id;
        this.handler = handler;
        this.users = new ArrayList<>();
        this.users.add(admin);
        this.lobbyDim = lobbyDim;
        isGameStarted = false;
        isEnded = false;
        this.start();
    }

    public Lobby(int id, User admin, int lobbyDim) {
        this.id = id;
        this.users = new ArrayList<>();
        this.users.add(admin);
        this.lobbyDim = lobbyDim;
        isGameStarted = false;
        isEnded = false;
        this.start();
    }

    @Override
    public void run() {
        while (!isGameStarted) { //TODO fai funzione waitfor. isGameStarted deve essere un semaforo
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        String[] arr = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            arr[i] = users.get(i).toString();
        }

        gameController = new Controller(this, arr);
    }

    public synchronized void startGame() {
        isGameStarted = true;
        notifyAll();
    }


    /**
     * Adds the user in the lobby.
     *
     * @param user The user to add to the lobby.
     * @return {@code true} if the user has been added, {@code false} otherwise.
     */
    public synchronized boolean addUser(User user) {
        if (users.size() < lobbyDim) { //!server.users.contains(user)
            users.add(user);
            user.setLobbyId(this.id);
            return true;
        }
        return false;
    }

    /**
     * Removes a user from the {@code Lobby}.
     *
     * @param user The username of the user to remove.
     */
    public synchronized void removeUser(String user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).toString().equals(user)) {
                users.get(i).setLobbyId(-1);
                users.remove(users.get(i));
            }
        }
    }

    /**
     * @return The usernames of the users in the {@code Lobby}.
     */
    public synchronized String[] getUsers() {
        String[] r = new String[users.size()];

        for (int i = 0; i < r.length; i++) {
            r[i] = users.get(i).toString();
        }

        return r;
    }

    /**
     * Sends a message to all users in the {@code Lobby}.
     *
     * @param message The message to send.
     */
    public void sendToLobby(Message message) throws IOException {
        for (User user : users) {
            user.send(message);
        }
    }

    public void sendStart() throws IOException {
        for (User u : users) {
            u.send(gameController.createStart(u.getUsername()));
        }
    }

    /**
     * @return The number of users in the {@code Lobby}.
     */
    public int getNumUsers() {
        return users.size();
    }


    /**
     * Called when the {@code Tile} selected are received by the {@code Player}.
     * Saves the tile in {@code selectedTiles} and notifies the controller.
     *
     * @param tiles the {@code Tile} selected by the {@code Player}.
     */
    public synchronized void onTileReceived(Tile[] tiles) {
        gameController.onTileReceived(tiles);
    }

    /**
     * Called when a column is received by the player.
     * Saves the column in {@code selectedColumn} and notifies the controller.
     *
     * @param selectedColumn the column selected by the {@code Player}.
     */
    public void onColumnReceived(int selectedColumn) throws IOException {
        gameController.onColumnReceived(selectedColumn);
    }

    public void sendAvailableTiles(String player, Tile[][] availableTiles) {
        for (User user : users) {
            if (user.equals(player)) {
                try {
                    user.send(new TilesRequest(availableTiles));
                } catch (IOException e) {
                    Logger.error("Available tiles not sent!");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void sendAvailableColumns(String player, int[] availableColumns) {
        for (User user : users) {
            if (user.equals(player)) {
                try {
                    user.send(new ColumnRequest(availableColumns));
                } catch (IOException e) {
                    Logger.error("Available tiles not sent!");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @return the {@code Player} whose turn it is
     */
    public String getCurrPlayer() {
        return gameController.getCurrPlayer();
    }

    public String getAdmin() {
        if (!isEnded) {
            return getUsers()[0];
        } else {
            return "";
        }
    }


    public void updateChat(ChatMessage s) throws IOException {
        if (Objects.equals(s.getReceiver(), "")) {
            for (User u : users) {
                u.chat.add(s);
                u.sendChat();
            }
        } else {
            for (User u : users) {
                if (u.getUsername().equals(s.getReceiver())) {
                    u.chat.add(s);
                    u.sendChat();
                    break;
                }
            }
        }

    }

    public void switchHandler(ClientHandler c, String username) throws IOException {
        for (User u : users) {
            if (Objects.equals(u.getUsername(), username)) {
                c.setGameState(GameState.IN_GAME);
                u.setClient(c);
                c.send(new StateUpdate(GameState.INSIDE_LOBBY));
                c.send(new LobbyData(id, getUsers()));
                sendStart();
                u.sendChat();
                gameController.onClientSwitched(c);
                if (nConnectedUsers() == 2)
                    gameController.awake();
                break;
            }
        }
    }

    public boolean userConnected(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.isConnected())
                return true;
        }
        return false;
    }

    public void resetChat() throws IOException {
        for (User u : users) {
            u.chat.clear();
        }
    }

    public void skip(String username) {
        gameController.skip(username);
    }

    public int nConnectedUsers() {
        int i = 0;
        for (User u : users) {
            if (u.isConnected())
                i++;
        }
        return i;
    }

    public void timer() {
        Timer timer = new Timer();
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        lastTimer = now;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (nConnectedUsers() == 1 && lastTimer == now) {
                    for (User u : users) {
                        if (u.isConnected()) {
                            try {
                                u.send(new StringRequest("The game is over!\nThe winner is: " + u.getUsername() + "!"));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    isEnded = true;
                    gameController.interrupt();
                    handler.removeLobby(id);
                }
            }
        };

        timer.schedule(task, 60000);
    }

    public void openChat(String username) {
        for (User u : users) {
            if (username.equals(u.getUsername())) {
                u.chat = new ArrayList<>();
                break;
            }
        }
    }
}
