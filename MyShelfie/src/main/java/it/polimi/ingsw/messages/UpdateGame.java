package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Tile;

import java.util.HashMap;

public class UpdateGame extends Message {
    public final String playerTurn;
    private final HashMap<String, Tile[][]> shelves;
    private Tile[][] board;
    private int[] commonGoals;

    /**
     * Constructs a {@code Message updateGame} containing the model's updates to send to the {@code Lobby}.
     * Is used to update the client with the model's updates.
     *
     * @param player the {@code Player} whose turn it is.
     */
    public UpdateGame(String player) {
        super(MessageType.UPDATE_GAME);
        playerTurn = player;
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

    public int[] getCommonGoals() {
        return commonGoals;
    }

    public void setCommonGoals(int[] commonGoals) {
        this.commonGoals = commonGoals;
    }
}
