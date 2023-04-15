package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Game;

public class StartRequest extends Message {
    private final Game currentGame;

    public StartRequest(Game currentGame) {
        super(ResponseType.START);
        this.currentGame = currentGame;
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
