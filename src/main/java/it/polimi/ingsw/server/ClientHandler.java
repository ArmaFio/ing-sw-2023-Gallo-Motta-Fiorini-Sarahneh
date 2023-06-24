package it.polimi.ingsw.server;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.Message;

import java.io.IOException;

public interface ClientHandler {
    String getAddress();

    boolean isConnected();

    void send(Message m) throws IOException;

    int GetId();

    boolean equals(ClientHandler other);

    GameState getGameState();

    void setGameState(GameState state);
}