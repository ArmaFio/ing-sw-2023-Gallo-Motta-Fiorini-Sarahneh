package it.polimi.ingsw.messages;

import it.polimi.ingsw.GameState;

public class UpdateState extends Message {
    public final GameState newState;

    public UpdateState(GameState newState) {
        super(ResponseType.UPD_STATE);
        this.newState = newState;
    }
}
