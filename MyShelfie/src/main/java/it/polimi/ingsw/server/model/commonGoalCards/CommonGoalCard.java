package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.shelf.Shelf;

public abstract class CommonGoalCard {
    int id;
    private int n_solved = 0;

    /**
     * Function to calculate the points obtained given a configuration of a player's shelf.
     * Needs to be Override.
     *
     * @param s the {@code Shelf} object of the {@code Player} to calculate the points from
     * @return An integer representing the points earned
     * @author Gallo Matteo
     */
    public int check_objective(Shelf s) {
        return -1; //TODO provare a cercare un'alternativa nell'etere
    }

    /**
     * If an objective is solved this method gives the number of points earned.
     *
     * @return The number of points earned.
     * @author Gallo Matteo
     */
    int addPoints() {
        n_solved++;
        return 8 - 2 * (n_solved - 1);
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
}
