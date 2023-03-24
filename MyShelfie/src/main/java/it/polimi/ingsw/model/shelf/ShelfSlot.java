package it.polimi.ingsw.model.shelf;

import it.polimi.ingsw.model.Tile;

public class ShelfSlot {
    private Tile tile;
    private boolean isEmpty;


    /**
     * The {@code ShelfSlot} of the {@code Shelf} that contains a {@code Tile}, can be empty
     *
     * @author Gallo Matteo
     */
    public ShelfSlot() {
        tile = null;
        isEmpty = true;
    }


    /**
     * @return {@code True} if the {@code ShelfSlot} doesn't contain a {@code Tile}
     * @author Gallo Matteo
     */
    public boolean isEmpty() {
        return isEmpty;
    }


    /**
     * @return The {@code Tile} contained in the {@code ShelfSlot}
     * @author Gallo Matteo
     */
    public Tile getTile() {
        return tile;
    }


    /**
     * Puts the {@code Tile} in the {@code ShelfSlot}
     *
     * @param t the {@code Tile} to put
     * @author Gallo Matteo
     */
    public void setTile(Tile t) {
        tile = t;
        isEmpty = false;
    }


    /**
     * Picks the {@code Tile} from the {@code ShelfSlot}
     *
     * @return The {@code Tile} removed from the {@code ShelfSlot}
     * @author Gallo Matteo
     */
    public Tile removeTile() {
        Tile temp = tile;

        tile = null;
        isEmpty = true;

        return temp; //TODO Forse deve essere void
    }
}
