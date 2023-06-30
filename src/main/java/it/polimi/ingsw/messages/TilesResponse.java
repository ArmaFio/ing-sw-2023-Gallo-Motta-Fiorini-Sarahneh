package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.tiles.Tile;

public class TilesResponse extends Message {
    private final Tile[] tilesSelected;

    /**
     * Constructs a {@code TilesResponse} containing the {@code Tile}s picked from the {@code Board}.
     * Is used to send the server which {@code Tile}s the client picked.
     *
     * @param tilesSelected the {@code Tile}s picked.
     */
    public TilesResponse(Tile[] tilesSelected) {
        super(MessageType.TILES_RESPONSE);
        this.tilesSelected = tilesSelected;
    }

    public Tile[] getSelectedTiles() {
        return tilesSelected;
    }
}
