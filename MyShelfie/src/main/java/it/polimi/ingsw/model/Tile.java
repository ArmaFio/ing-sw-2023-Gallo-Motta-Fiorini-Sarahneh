package it.polimi.ingsw.model;

public class Tile {

    public final TileType type;

    /**
     * Constructor of the tile, set it's color.
     * @param color color of the tile.
     */
    public Tile(TileType color){
        this.type = color;
    }

    /**
     * Method to get the color of the tile.
     *
     * @return color of the tile.
     */
    public TileType getType() {
        return type;
    }
}


