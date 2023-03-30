package it.polimi.ingsw.model;

public class Tile {

    public final TileType type;
    public final int id;

    /**
     * Constructor of the tile, set its type.
     *
     * @param type type of the tile.
     */
    public Tile(TileType type, int id) {
        this.type = type;
        this.id = id;
    }

    public Tile(TileType type) {
        this(type, -1);
    }

    public Tile() {
        this(TileType.NONE);
    }

    /**
     * Method to get the color of the tile.
     *
     * @return color of the tile.
     */
    public TileType getType() {
        return type;
    }


    public boolean equals(Tile other) {  //N.B not an override
        return this.id == other.id;
    }

}


