package it.polimi.ingsw.model;

import it.polimi.ingsw.model.shelf.Shelf;

import java.lang.String;

/**
 * @author Armando Fiorini
 */
public class Player {
    private final Shelf shelf;
    private String username;
    private int points;
    private PersonalGoalCard pgc;

    /**
     * constructs a player
     *
     * @param user player's username
     * @param po   player's personal objective card
     */
    public Player(String user, PersonalGoalCard po) {
        username = user;
        points = 0;
        pgc = po;
        shelf = new Shelf();
    }

    /**
     * updates player's score adding the passed number of points
     *
     * @param nop number of points to add
     */
    public void add_points(int nop) {
        this.points = this.points + nop;
    }

    /**
     * @return player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return player's current score
     */
    public int getPoints() {
        return points;
    }

    public Shelf getShelf() {
        return shelf;
    }
}