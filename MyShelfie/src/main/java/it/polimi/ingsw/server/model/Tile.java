package it.polimi.ingsw.server.model;

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

    public Tile(Tile tile) {
        this(tile.type, tile.id);
    }

    public Tile() {
        this(TileType.NONE);
    }


    public boolean equalsId(Tile other) {  //N.B not an override
        return this.id == other.id;
    }

    public boolean equalsType(Tile other) {  //N.B not an override
        return this.type.equals(other.type);
    }

    @Override
    public String toString() {
        return (this.id + " -> " + this.type.toString());
    }
}


