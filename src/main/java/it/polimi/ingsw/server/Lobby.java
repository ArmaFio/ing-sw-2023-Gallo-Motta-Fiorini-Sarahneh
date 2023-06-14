package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Chat;
import it.polimi.ingsw.messages.ColumnRequest;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.TilesRequest;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class Lobby extends Thread {
    public final int id;
    private final ArrayList<User> users;
    public final int lobbyDim;
    private boolean isGameStarted;
    private Controller gameController;
    private boolean isEnded;
    private final ArrayList<String> chat;

    public Lobby(int id, User admin, int lobbyDim) {
        this.id = id;
        this.users = new ArrayList<>();
        this.users.add(admin);
        this.chat = new ArrayList<>();
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
     * @return {@code true} if the remove succeed, {@code false} if the remove failed
     */
    public synchronized boolean removeUser(String user) { //TODO fai gestire da LobbiesHandler così cancella la lobby se finiscono gli utenti
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).toString().equals(user)) {
                users.remove(users.get(i));
                return true;
            }
        }
        return false;
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
    public void onColumnReceived(int selectedColumn) {
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


    public void close() {
        isEnded = true;
        //TODO da fare. la lobby termina e anche il controller.
    }

    public String getAdmin() {
        if (!isEnded) {
            return getUsers()[0];
        } else {
            return "";
        }
    }


    public void updateChat(String s) throws IOException {
        chat.add(s);
        String[] arrayChat = chat.toArray(String[]::new);
        sendToLobby(new Chat(arrayChat));
    }
}
