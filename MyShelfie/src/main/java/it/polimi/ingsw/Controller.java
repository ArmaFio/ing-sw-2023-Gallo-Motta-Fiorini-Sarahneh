package it.polimi.ingsw;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ClientHandler;

public class Controller extends Thread implements Observer {
    private Game game;
    private Lobby lobby;
    private String[] users;
    private ClientHandler server;

    public Controller(ClientHandler server, String[] users) {
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
