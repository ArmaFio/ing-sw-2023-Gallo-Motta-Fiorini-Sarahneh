package it.polimi.ingsw.client;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.model.tiles.TileType;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

public interface View extends Serializable {
    void onLobbyListMessage(LobbiesList msg);

    GameState getGameState();

    /**
     * Updates the game state
     *
     * @param newState new game state
     */
    void updateState(GameState newState);

    void setClient(NetworkHandler client);

    void onStringRequest(StringRequest message);

    void updateState();

    void onStringMessage(String message);

    void updatePhase(GamePhase newPhase);

    /**
     * Informs the client that the lobby has been joined and shows the other players being in
     *
     * @param lobbyUsers array containing lobby members' usernames
     */
    void onLobbyDataMessage(String[] lobbyUsers);

    void write(Message message) throws IOException;

    /**
     * Sets the available {@code Tile}s for the client, received by the server.
     *
     * @param availableTiles The {@code Tile}s received.
     */
    void onAvailableTiles(Tile[][] availableTiles);

    /**
     * Sets the available columns for the client, received by the server.
     *
     * @param availableColumns The columns received.
     */
    void onAvailableColumns(int[] availableColumns);

    HashMap<String, Tile[][]> getShelves();

    String getUsername();

    void setUsername(String username);

    void setPersonalGoal(TileType[][] personalGoal, int personalId);

    void setCommonGoals(HashMap<Integer, String> commonsGoals);

    void onGameUpdate(GameUpdate update);

    void setPassword(String s);

    void disconnect();

    void onPointsMessage(PointsUpdate points);

    void onChatUpdate(ChatMessage[] chat);
}
