package it.polimi.ingsw.client;

import java.util.HashMap;

public class CommonGoalCard {
    public final int id;
    public final String description;
    private HashMap<String, Integer> solvers;

    public CommonGoalCard(int id, String description){
        this.id = id;
        this.description = description;
    }

    public void updateSolvers(HashMap<String, Integer> solvers){
        this.solvers  = solvers;
    }

}
