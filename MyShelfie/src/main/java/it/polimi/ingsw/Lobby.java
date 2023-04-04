package it.polimi.ingsw;

import java.util.ArrayList;

public class Lobby extends Thread implements Observer {
    public final int id;
    private ArrayList<User> users;
    private SocketServer server;
    private boolean isGameStarted;

    public Lobby(int id, User admin, SocketServer server) {
        this.id = id;
        users.add(admin);
        this.server = server;
        isGameStarted = false;
        this.start();
    }

    @Override
    public void run() {
        //TODO wait for connections
        while (!isGameStarted) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void update() {
        updateUsers();

        if (server.data.isGameStarted(id)) {
            isGameStarted = true;
        }

    }

    private void updateUsers() {
        User newUser = server.data.getNewUser(id);
        if (!newUser.isNone()) {
            users.add(newUser);
        }
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}
