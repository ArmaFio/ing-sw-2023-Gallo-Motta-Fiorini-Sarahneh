package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Tile;

public class TilesResponse extends Message {
    private final Tile[] tilesSelected;

    public TilesResponse(Tile[] tilesSelected) {
        super(MessageType.TILES_RESPONSE);
        this.tilesSelected = tilesSelected;
    }

    public Tile[] getSelectedTiles() {
        return tilesSelected;
    }
}
