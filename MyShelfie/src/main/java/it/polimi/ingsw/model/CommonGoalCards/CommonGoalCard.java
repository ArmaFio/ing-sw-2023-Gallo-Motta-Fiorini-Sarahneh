package it.polimi.ingsw.model.CommonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;

public abstract class CommonGoalCard {

    /**
     * Function to calculate the points obtained given a configuration of a player's shelf
     *
     * @param s the {@code Shelf} object of the {@code Player} to calculate the points from
     * @return An integer representing the points earned
     * @author Gallo Matteo
     */
    public int check_objective(Shelf s) {
        return -1; //TODO provare a cercare un'alternativa nell'etere
    }
}
