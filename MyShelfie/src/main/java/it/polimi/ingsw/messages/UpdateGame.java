package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Tile;

import java.util.HashMap;

public class UpdateGame extends Message {
    private final HashMap<String, Tile[][]> shelves;
    private Tile[][] board;

    public UpdateGame(String player) {
        super(MessageType.UPDATE_GAME);
        shelves = new HashMap<>();
    }

    public void addShelf(String username, Tile[][] shelf) {
        this.shelves.put(username, shelf);
    }

    public Tile[][] getShelf(String username) {
        return shelves.get(username);
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }
}
