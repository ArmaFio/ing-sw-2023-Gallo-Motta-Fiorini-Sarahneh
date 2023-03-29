package it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientSocket {
    Socket socket;

    public ClientSocket() throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 59090);) //open a socket
        {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            //TODO add output and continue implementation

        }
    }
}
