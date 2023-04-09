package it.polimi.ingsw.messages;

import it.polimi.ingsw.model.Tile;

import java.util.ArrayList;

public class TilesRequest extends Message {
    private final ArrayList<ArrayList<Tile>> available;
    private ArrayList<Tile> chosen;
    private int col;

    public TilesRequest(ArrayList<ArrayList<Tile>> available) {
        this.available = available;
        author = null;
        type = ResponseType.TILES;
    }

    public void setChosen(ArrayList<Tile> chosen) {
        this.chosen = chosen;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ArrayList<ArrayList<Tile>> getAvailable() {
        return available;
    }
}
