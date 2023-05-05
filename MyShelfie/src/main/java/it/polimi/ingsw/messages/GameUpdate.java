package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.model.Tile;

import java.util.HashMap;

public class GameUpdate extends Message {
    public final String playerTurn;
    private final HashMap<String, Tile[][]> shelves;
    private Tile[][] board;
    private HashMap<Integer, HashMap<String, Integer>> commonGoals; //TODO vedi se conviene creare una classe CommonData

    /**
     * Constructs a {@code Message updateGame} containing the model's updates to send to the {@code Lobby}.
     * Is used to update the client with the model's updates.
     *
     * @param player the {@code Player} whose turn it is.
     */
    public GameUpdate(String player) {
        super(MessageType.UPDATE_GAME);
        playerTurn = player;
        shelves = new HashMap<>();
    }

    public void addShelf(String username, Tile[][] shelf) {
        this.shelves.put(username, shelf);
    }

    public HashMap<String, Tile[][]> getShelves() {
        return shelves;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    public HashMap<Integer, HashMap<String, Integer>> getCommonGoals() {
        return commonGoals;
    }

    public void setCommonGoals(HashMap<Integer, HashMap<String, Integer>> commonGoals) {
        this.commonGoals = commonGoals;
    }
}
