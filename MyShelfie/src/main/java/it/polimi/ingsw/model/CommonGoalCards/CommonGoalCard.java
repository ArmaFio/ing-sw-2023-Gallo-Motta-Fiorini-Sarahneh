package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;

interface CommonGoalCard {

    /**
     * Function to calculate the points obtained given a configuration of a player's shelf
     *
     * @param s the {@code Shelf} object of the {@code Player} to calculate the points from
     * @return An integer representing the points earned
     * @author Gallo Matteo
     */
    int check_objective(Shelf s);
}
