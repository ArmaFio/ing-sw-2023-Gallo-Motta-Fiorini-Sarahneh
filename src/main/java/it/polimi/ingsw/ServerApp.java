package it.polimi.ingsw;


import it.polimi.ingsw.server.MainServer;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        try {
            new MainServer();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}