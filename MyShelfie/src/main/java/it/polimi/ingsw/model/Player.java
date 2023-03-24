package it.polimi.ingsw.model;

import java.lang.String;

public class Player{
    private final String username;
    private Shelf shelf;
    private int points;
    private PersonalGoalCard pgc;

    //initializes Player from username and Personal Objective
    public Player(String user,PersonalGoalCard po){
        username=user;
        points=0;
        pgc= po;
        shelf= new Shelf();
    }

    //Upgrades Player's score adding the number of points indicated by the arguments
    public void add_points(int nop){
        this.points=this.points+nop;
    }

    //Returns Player's username
    public String getUsername() {
        return username;
    }

    //Returns Player's current score
    public int getPoints() {
        return points;
    }
}