package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Tile;

/**
 * Message used for asking to pick the tiles from the ones available
 */
public class TilesRequest extends Message {
    private final Tile[][] tilesAvailable;

    public TilesRequest(Tile[][] tiles) {
        super(MessageType.TILES_REQUEST);
        this.tilesAvailable = tiles;
    }

    public Tile[][] getAvailableTiles() {
        return tilesAvailable;
    }
}
