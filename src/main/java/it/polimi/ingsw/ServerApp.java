package it.polimi.ingsw;


import it.polimi.ingsw.server.network.SocketMainServer;

import java.io.IOException;
import java.rmi.AlreadyBoundException;

public class ServerApp {
    public static void main(String[] args) {
        try {
            new SocketMainServer();
        } catch (IOException | InterruptedException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }
}