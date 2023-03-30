package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;

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

    public boolean equals(CommonGoalCard other) {
        return this.id == other.id;
    }
}
