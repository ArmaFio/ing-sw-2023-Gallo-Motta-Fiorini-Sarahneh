package it.polimi.ingsw.model;

public class BoardSlot {
    private Tile tile;
    private boolean taken; //is the tile already taken?
    private final boolean usable;  //is a valid slot in the board?

    /**
     * Constructor used by Board to initialize the board when a new game starts.
     *
     * @param usable tells if it's possible to put tiles in this slot.
     * @param bag    the bag from which the tile for this slot is drawn.
     */
    public BoardSlot(boolean usable, Bag bag) {  //TODO Perchè passare bag come parametro?
        this.usable = usable;
        if (!usable) {
            tile = null;
        } else {
            tile = bag.draw();
            taken = false;
        }
    }

    public void setTile(Tile t) {
        tile = t;
    }

    /**
     * Tells if there still is a tile in this slot.
     *
     * @return true if there is no longer a tile in this slot, false otherwise.
     */
    public boolean occupied() {
        return taken;
    }

    /**
     * Set the availability of a tile in this {@code BoardSlot}.
     * @param taken if true, there is not an available tile on this {@code BoardSlot}; if false, the tile in this {@code BoardSlot} is available.
     */
    @Deprecated
    public void setTaken(boolean taken){
        this.taken = taken;
    }

    /**
     * Getter for the tile.
     * @return the tile assigned to this slot.
     */
    public Tile getSlotTile(){
        return tile;
    }

    /**
     * Tells if the slot can have a tile.
     *
     * @return true if the slot can have a tile, false otherwise.
     */
    public boolean isUsable() {
        return usable;
    }

    public void removeTile() {
        taken = true;
        tile = new Tile(TileType.NONE, -1);
    }
}
