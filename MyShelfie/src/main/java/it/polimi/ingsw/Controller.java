package it.polimi.ingsw;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.model.Game;

public class Controller extends Thread implements Observer {
    private Game game;
    private Lobby lobby;
    private String[] users;

    public Controller(String[] users) {
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
