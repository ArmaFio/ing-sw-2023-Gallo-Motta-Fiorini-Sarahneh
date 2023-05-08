package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.TileType;

public class StartMessage extends Message {
    TileType[][] pgc;


    public StartMessage(TileType[][] matrix) {
        super(MessageType.START);
        this.pgc = matrix;
    }

    public TileType[][] getPgc() {
        return pgc;
    }
}
