package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.shelf.Shelf;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Armando Fiorini.
 */
public class Player {
    public final PersonalGoalCard personalGoalCard;
    private final Shelf shelf;
    private final String username;
    private int points;

    /**
     * Constructs a player.
     *
     * @param user player's username.
     * @param pgc  player's personal goal card.
     */
    public Player(String user, PersonalGoalCard pgc) {
        username = user;
        points = 0;
        this.personalGoalCard = pgc;
        shelf = new Shelf(username);
    }

    /**
     * Updates player's score adding the passed number of points.
     *
     * @param points number of points to add.
     */
    public void addPoints(int points) {
        this.points = this.points + points;
    }

    /**
     * Checks the personal goal level of completion and assigns the correct amount of points to the player.
     */
    public void checkObjective() {
        addPoints(personalGoalCard.checkObjective(shelf));
    }

    /**
     * @return player's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return player's current score.
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return player's shelf.
     */
    public Shelf getShelfObj() { //TODO va tolto
        return shelf;
    }

    public Tile[][] getShelf() {
        return shelf.getShelf();
    }

    public synchronized Tile[][] filter(Tile[][] tiles) {
        int maxTiles = shelf.getMaxColumns();
        ArrayList<ArrayList<Tile>> result = new ArrayList<>();
        for (Tile[] t : tiles) {
            if (t.length <= maxTiles) {
                ArrayList<Tile> temp = new ArrayList<>(Arrays.asList(t));
                result.add(temp);
            }
        }
        return result.stream().map(e -> e.toArray(new Tile[0])).toArray(Tile[][]::new);
    }

    /**
     * Adds points to the {@code Player} according to the groups of tiles it has formed (Adjacent Item tiles).
     */
    public void checkGroups() {
        int[] dim_groups = shelf.find_groups();

        for (int dim : dim_groups) {
            if (dim == 3) {
                addPoints(2);
            }
            if (dim == 4) {
                addPoints(3);
            }
            if (dim == 5) {
                addPoints(5);
            }
            if (dim >= 6) {
                addPoints(8);
            }
        }
    }

    public void putTilesInShelf(int column, Tile[] tiles) {
        shelf.putTiles(column, tiles);
    }

    public int[] getAvailableColumns(int nTiles) {
        return shelf.availableColumns(nTiles);
    }


}