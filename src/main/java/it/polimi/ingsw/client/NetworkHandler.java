package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.Message;

import java.io.IOException;

public interface NetworkHandler {
    void setInit(boolean value);

    void write(Message m) throws IOException;

    void disconnect();

    void reconnect();

    void setUsername(String s);
}
