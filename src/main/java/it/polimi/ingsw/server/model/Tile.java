package it.polimi.ingsw.server.model;

import java.io.Serializable;
import java.rmi.Remote;

public class Tile implements Serializable, Remote {

    public final TileType type;
    public final int id;
    public int x;
    public char y;

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

    public Tile(TileType type, int id, int x, char y) {
        this.type = type;
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Tile(TileType type) {
        this(type, -1);
    }

    public Tile(Tile tile) {
        this(tile.type, tile.id, tile.x, tile.y);
    }

    public Tile() {
        this(TileType.EMPTY);
    }

    public boolean isNone() {
        return this.type == TileType.NONE;
    }

    public boolean equalsId(Tile other) {  //N.B not an override
        return this.id == other.id;
    }

    public boolean equalsType(Tile other) {  //N.B not an override
        return this.type.equals(other.type);
    }

    @Override
    public String toString() {
        return ("(" + this.x + " ; " + this.y + ") -> " + this.type.toString());
    }

    public String toStringShort() {
        return this.type.toString();
    }

    public void setpos(int x, char y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEmpty() {
        return type == TileType.EMPTY;
    }

    public TileType getType() {
        return type;
    }


}


