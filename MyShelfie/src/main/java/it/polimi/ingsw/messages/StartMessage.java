package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.TileType;

import java.util.HashMap;

public class StartMessage extends Message {
    private final TileType[][] personalGoal;
    private final HashMap<Integer, String> commonsGoals;


    public StartMessage(TileType[][] personalGoal,  HashMap<Integer, String> commonsGoals) {
        super(MessageType.START);
        this.personalGoal = personalGoal;
        this.commonsGoals = commonsGoals;
    }

    public TileType[][] getPersonalGoal() {
        return personalGoal;
    }

    public HashMap<Integer, String> getCommonsGoals() {
        return commonsGoals;
    }
}
