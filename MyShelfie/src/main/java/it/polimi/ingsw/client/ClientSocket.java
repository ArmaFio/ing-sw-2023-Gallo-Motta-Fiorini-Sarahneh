package it.polimi.ingsw.client;

import java.io.*;
import java.net.Socket;

public class ClientSocket {
    private final BufferedReader reader;
    private final PrintWriter writer;
    private Socket socket;
    private final ClientView View;

    public ClientSocket() throws IOException {
        try (Socket socket = new Socket("127.0.0.1", 59090);) {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
            View = new ClientView();
        }
    }

    public void send(String s) {
        writer.println(s);
    }

    public String read() throws IOException {
        return reader.readLine();
    }

    public void close() throws IOException {
        socket.close();
    }
}
