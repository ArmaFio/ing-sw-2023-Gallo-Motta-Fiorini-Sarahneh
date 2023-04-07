package it.polimi.ingsw.model;

public class Tile {

    public final TileType type;
    public final int id;

    /**
     * Constructor of the tile, set its type.
     *
     * @param type type of the tile.
     * @param id   id of the tile (unique for each of the 122 tiles).
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


    public boolean equals(Tile other) {  //N.B not an override
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return (this.id + " -> " + this.type.toString());
    }
}


