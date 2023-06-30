package it.polimi.ingsw.server.model.tiles;

import java.util.ArrayList;
import java.util.Random;

public class TilesBag {
    private final ArrayList<Tile> tiles = new ArrayList<>();
    private int remainingTiles;

    /**
     * Constructor: fills the empty bag with 132 tiles (22 for each type) at the beginning of a game.
     */
    public TilesBag() {
        remainingTiles = 132;
        int id = 0;
        for (int i = 0; i < 22; i++) {
            for (TileType t : TileType.values()) {
                if (!t.equals(TileType.EMPTY) && !t.equals(TileType.NONE)) {
                    Tile drawn = new Tile(t, id);
                    id++;
                    tiles.add(drawn);
                }
            }
        }
    }

    /**
     * Draw a random tile from the bag.
     *
     * @return tile drawn.
     */
    public Tile draw() {
        if (tiles.size() == 0) {
            return new Tile(TileType.EMPTY);
        }
        Random indexGen = new Random();
        int index = indexGen.nextInt(remainingTiles);
        Tile t = tiles.get(index);
        tiles.remove(index);
        remainingTiles--;
        return t;
    }

    public ArrayList<Tile> getTiles() {  //only for testing purposes
        return tiles;
    }
}
