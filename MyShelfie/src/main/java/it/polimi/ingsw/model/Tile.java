package it.polimi.ingsw.model;

public class Tile {

    TileType color;

    /**
     * Constructor of the tile, set it's color.
     * @param color color of the tile.
     */
    public Tile(TileType color){
        this.color = color;
    }

    /**
     * Method to get the color of the tile.
     * @return color of the tile.
     */
    public TileType getColor(){
        return  color;
    }
}
