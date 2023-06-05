package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.Message;

import java.io.IOException;

public abstract class NetworkHandler extends Thread {
    public static boolean init = false;

    abstract void write(Message m) throws IOException;

    abstract void disconnect();

    abstract void reconnect();

    abstract void setUsername(String s);
}
