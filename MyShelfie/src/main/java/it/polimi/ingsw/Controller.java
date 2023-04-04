package it.polimi.ingsw;

import it.polimi.ingsw.model.Game;

public class Controller extends Thread implements Observer {
    private Game game;
    private Lobby lobby;
    private User[] users;
    private SocketServer server;

    public Controller(SocketServer server, User[] users) {
        this.server = server;
        this.users = users;
        this.start();
    }

    @Override
    public void run() {
        game = new Game(users);
    }

    @Override
    public void update() {

    }
}
