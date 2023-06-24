package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.rmi.Remote;

public class Tile implements Serializable, Remote {

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
        this.type = tile.type;
        this.id = tile.id;
    }

    public Tile() {
        this(TileType.EMPTY);
    }

    public boolean isNone() {
        return this.type.equals(TileType.NONE);
    }

    public boolean equalsId(Tile other) {  //N.B not an override
        return this.id == other.id;
    }

    public boolean equalsType(Tile other) {  //N.B not an override
        return this.type.equals(other.type);
    }

    public boolean isEmpty() {
        return type.equals(TileType.EMPTY);
    }
}


