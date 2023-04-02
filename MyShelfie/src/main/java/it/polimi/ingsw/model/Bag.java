package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Bag {
    private final ArrayList<Tile> tiles = new ArrayList<Tile>();
    private final Random indexGen = new Random();
    private int remainingTiles;

    /**
     * Constructor: fills the empty bag with 132 tiles (22 for each type) at the beginning of a game.
     */
    public Bag() {  //insert all 132 tiles in the bag
        remainingTiles = 132;
        int id = 0;
        for (int i = 0; i < 22; i++) {
            for (TileType t : TileType.values()) {
                if (!t.equals(TileType.NONE)) {
                    Tile drawn = new Tile(t, id);
                    id++;
                    tiles.add(drawn);
                }
            }
        }
    }

    /**
     * Draw a random tile from the bag.
     * @return tile drawn.
     */
    public Tile draw(){
        int index = indexGen.nextInt(remainingTiles);
        Tile t = tiles.get(index);
        tiles.remove(index);
        remainingTiles--;
        return t;
    }


    public ArrayList<Tile> getTiles(){  //only for testing purposes
        return tiles;
    }
}
