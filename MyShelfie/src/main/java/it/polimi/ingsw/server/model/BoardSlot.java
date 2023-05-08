package it.polimi.ingsw.server.model;

public class BoardSlot {
    private final boolean usable;  //is a valid slot in the board?
    private Tile tile;
    private boolean taken; //is the tile already taken?

    /**
     * Constructor used by Board to initialize the board when a new game starts.
     *
     * @param usable tells if it's possible to put tiles in this slot.
     * @param tile   the {@code Tile} for this slot.
     */
    public BoardSlot(boolean usable, Tile tile) {
        this.usable = usable;
        if (!usable) {
            removeTile();
        } else {
            this.tile = tile;
            taken = false;
        }
    }

    /**
     * Tells if there still is a tile in this slot.
     *
     * @return true if there is no longer a tile in this slot, false otherwise.
     */
    public boolean isOccupied() {
        return taken;
    } //TODO chiamalo isEmpty()

    /**
     * Set the availability of a tile in this {@code BoardSlot}.
     *
     * @param taken if true, there is not an available tile on this {@code BoardSlot}; if false, the tile in this {@code BoardSlot} is available.
     */
    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    /**
     * Getter for the tile.
     *
     * @return the tile assigned to this slot.
     */
    public Tile getTile() {
        if (usable) {
            return new Tile(tile);
        } else {
            return new Tile();
        }
    }

    /**
     * Sets the tile for this slot.
     *
     * @param t {@code Tile} we want to assign to this {@code BoardSlot}.
     */
    public void setTile(Tile t) {
        tile = t;
    }

    /**
     * Tells if the slot can have a tile.
     *
     * @return true if the slot can have a tile, false otherwise.
     */
    public boolean isUsable() {
        return usable;
    }

    /**
     * Removes the {@code Tile} from the current {@code BoardSlot}.
     */
    public void removeTile() {
        taken = true;
        tile = new Tile(TileType.NONE, -1);
    }

    public Tile returnTile() {
        return this.tile;
    }

    @Override
    public String toString() {
        return (this.tile.id + "\n" + this.tile.type.toString());
    }
}
