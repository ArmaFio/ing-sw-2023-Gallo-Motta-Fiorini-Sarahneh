package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Game;

@Deprecated
public class StartRequest extends Message {
    private final Game currentGame;

    public StartRequest(Game currentGame) {
        super(MessageType.START);
        this.currentGame = currentGame;
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
