package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Shelf;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CommonGoalCard {
    protected ArrayList<String> solvers;
    protected int nPlayers;
    protected int id;
    protected String description;
    private int nSolved = 0;

    protected CommonGoalCard(int nPlayers) {
        this.nPlayers = nPlayers;
        this.solvers = new ArrayList<>();
    }

    /**
     * Function to calculate the points obtained given a configuration of a player's shelf.
     * Needs to be Override.
     *
     * @param s the {@code Shelf} object of the {@code Player} to calculate the points from
     * @return An integer representing the points earned
     * @author Gallo Matteo
     */
    abstract public int check_objective(Shelf s);

    /**
     * If an objective is solved this method gives the number of points earned.
     *
     * @return The number of points earned.
     * @author Gallo Matteo
     */
    protected int addPoints(String username) {
        if (!solvers.contains(username)) {
            solvers.add(username);
            nSolved++;
            if (nPlayers > 2) {
                return 8 - 2 * (nSolved - 1);
            }
            return 8 - 4 * (nSolved - 1);
        }
        return 0;
    }

    /**
     * @return HashMap containing the players who solved the common goal card
     */
    public HashMap<String, Integer> getSolvers() {
        HashMap<String, Integer> solvers = new HashMap<>();
        int points = 8;

        for (String s : this.solvers) {
            solvers.put(s, points);
            points -= 2;
        }

        return solvers;
    }

    /**
     * Checks if two {@code CommonGoalCard } are the same looking at their id.
     *
     * @param other the {@code CommonGoalCard } compared to the current object.
     * @return true if the cards are the same (have the same id).
     */
    public boolean equals(CommonGoalCard other) {
        return this.id == other.id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
