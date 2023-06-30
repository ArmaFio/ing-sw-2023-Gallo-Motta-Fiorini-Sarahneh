package it.polimi.ingsw.server.model.GameT;

import it.polimi.ingsw.server.model.PersonalGoalCard;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.tiles.Tile;

/**
 * @author Armando Fiorini.
 */
public class TestablePlayer{
    public PersonalGoalCard pgc;
    private final Shelf shelf;
    private final String username;
    private int points;

    /**
     * constructs a player.
     *
     * @param user player's username.
     * @param pgc  player's personal objective card.
     */
    public TestablePlayer(String user, PersonalGoalCard pgc) {
        username = user;
        points = 0;
        this.pgc = pgc;
        shelf = new Shelf(username);
    }

    /**
     * updates player's score adding the passed number of points.
     *
     * @param points number of points to add.
     */
    public void add_points(int points) {
        this.points = this.points + points;
    }

    /**
     * Checks the personal goal level of completion and assigns the correct amount of points to the player.
     */
    public void check_objective() {
        add_points(pgc.checkObjective(shelf));
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

    public Shelf getShelfObj() {
        return shelf;
    }

    public Tile[][] getShelf() {
        return shelf.getShelf();
    }

    /**
     * Adds points to the {@code Player} according to the groups of tiles it has formed (Adjacent Item tiles).
     */
    public void check_groups() {
        int[] dim_groups = shelf.findGroups();

        for (int dim : dim_groups) {
            if (dim == 3) {
                add_points(2);
            }
            if (dim == 4) {
                add_points(3);
            }
            if (dim == 5) {
                add_points(5);
            }
            if (dim >= 6) {
                add_points(8);
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
