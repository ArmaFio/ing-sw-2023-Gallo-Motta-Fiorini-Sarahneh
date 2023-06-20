package it.polimi.ingsw.client;

import java.util.HashMap;

public class CommonGoalCard {
    public final int id;
    public final String description;
    private HashMap<String, Integer> solvers;

    /**
     * Constructor for the {@code CommonGoalCard}
     *
     * @param id          id of the card.
     * @param description goal description.
     */
    public CommonGoalCard(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Updates solvers of the common goal.
     *
     * @param solvers current solvers for the specified common goal.
     */
    public void updateSolvers(HashMap<String, Integer> solvers) {
        this.solvers = solvers;
    }

}
