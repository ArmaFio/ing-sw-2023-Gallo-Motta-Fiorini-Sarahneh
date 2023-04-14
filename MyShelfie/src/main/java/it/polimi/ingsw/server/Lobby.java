package it.polimi.ingsw.server;

import it.polimi.ingsw.Controller;

import java.util.ArrayList;

public class Lobby extends Thread {
    public final int id;
    private boolean isGameStarted;
    private Controller gameController;
    private ArrayList<String> users;

    public Lobby(int id, String admin) {
        this.id = id;
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


    /**
     * Adds the user in the lobby.
     * @param user The user to add to the lobby.
     * @return {@code true} if the user has been added, {@code false} otherwise.
     */
    public synchronized boolean addUser(User user) {
        if (users.size() < 4) { //!server.users.contains(user)
            users.add(user.username);
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
        if (users.contains(user)) {
            users.remove(user);
            return true;
        }
        return false;
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
