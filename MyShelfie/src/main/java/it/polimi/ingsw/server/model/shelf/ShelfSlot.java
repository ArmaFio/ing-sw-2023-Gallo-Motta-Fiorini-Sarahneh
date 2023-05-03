package it.polimi.ingsw.server.model.shelf;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;

@Deprecated
public class ShelfSlot { //TODO forse inutile
    private Tile tile;
    private boolean isEmpty;


    /**
     * The {@code ShelfSlot} of the {@code Shelf} that contains a {@code Tile}, can be empty.
     *
     * @author Gallo Matteo.
     */
    public ShelfSlot() {
        tile = new Tile();
        isEmpty = true;
    }


    /**
     * @return {@code True} if the {@code ShelfSlot} doesn't contain a {@code Tile}, false otherwise.
     * @author Gallo Matteo.
     */
    public boolean isEmpty() {
        return isEmpty;
    }


    /**
     * @return The {@code Tile} contained in the {@code ShelfSlot}.
     * @author Gallo Matteo.
     */
    public Tile getTile() {
        return new Tile(tile);
    }


    /**
     * Puts the {@code Tile} in the {@code ShelfSlot}.
     *
     * @param t the {@code Tile} to put.
     * @author Gallo Matteo.
     */
    public void setTile(Tile tile) {
        this.tile = tile;
        isEmpty = this.tile.type.equals(TileType.NONE);
    }


    /**
     * Picks the {@code Tile} from the {@code ShelfSlot}.
     *
     * @return The {@code Tile} removed from the {@code ShelfSlot}.
     * @author Gallo Matteo.
     */
    @Deprecated
    public Tile removeTile() {
        Tile temp = tile;

        tile = new Tile();
        isEmpty = true;

        return temp; //TODO Forse deve essere void
    }

    @Override
    public String toString() {
        return (this.tile.id + "\n" + this.tile.type.toString());
    }
}
