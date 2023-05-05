package it.polimi.ingsw.messages;

import it.polimi.ingsw.GameState;

public class StateUpdate extends Message {
    public final GameState newState;

    public StateUpdate(GameState newState) {
        super(MessageType.UPD_STATE);
        this.newState = newState;
    }
}
