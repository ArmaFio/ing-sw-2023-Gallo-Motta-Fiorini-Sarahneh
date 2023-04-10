package it.polimi.ingsw;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;

public class Lobby extends Thread {
    public final int id;
    private final Object mutex = new Object();
    private boolean isGameStarted;
    private Controller gameController;
    private ArrayList<String> users;

    public Lobby(String admin) {
        this.id = ClientHandler.lobbies.getNewId();
        this.users = new ArrayList<>();
        this.users.add(admin);
        isGameStarted = false;
        this.start();
    }


    @Override
    public void run() {
        while (!isGameStarted) {
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
            arr[i] = users.get(i);
        }
        gameController = new Controller(arr);

    }

    public void startGame() {
        isGameStarted = true;
        notifyAll();
    }

    public synchronized boolean addUser(String user) {
        if (users.size() < 4 && !ClientHandler.users.contains(user)) {
            users.add(user);
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
        if (ClientHandler.users.contains(user)) {
            users.remove(user);
            return true;
        }
        return false;
    }

    /**
     * Method that sends to all the users in the {@code Lobby} a message.
     *
     * @param msg The message to send.
     */
    public void sendAll(Message msg) throws IOException {
        for (String key : users) {
            ClientHandler.users.get(key).getServer().write(msg);
        }
    }

    /**
     * @return The usernames of the users in the {@code Lobby}.
     */
    public synchronized String[] getUsers() {
        String[] r = new String[users.size()];

        for (int i = 0; i < r.length; i++) {
            r[i] = users.get(i);
        }

        return r;
    }

    /**
     * @return The number of users in the {@code Lobby}.
     */
    public int getNumUsers() {
        return users.size();
    }
}
