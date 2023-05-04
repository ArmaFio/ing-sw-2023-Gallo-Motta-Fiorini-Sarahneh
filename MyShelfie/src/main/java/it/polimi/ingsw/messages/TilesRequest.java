package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Tile;

/**
 * Message used for asking to pick the tiles from the ones available
 */
public class TilesRequest extends Message {
    private final Tile[][] tilesAvailable;

    /**
     * Constructs a {@code TilesRequest} containing the available {@code Tile}s in the {@code Board}.
     * Is used to ask the client which {@code Tile}s of the {@code Board} he wants to place pick.
     *
     * @param tiles the available {@code Tile}s.
     */
    public TilesRequest(Tile[][] tiles) {
        super(MessageType.TILES_REQUEST);
        this.tilesAvailable = tiles;
    }

    public Tile[][] getAvailableTiles() {
        return tilesAvailable;
    }
}
