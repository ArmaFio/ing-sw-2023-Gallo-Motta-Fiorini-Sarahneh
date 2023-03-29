package it.polimi.ingsw;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean run;

    public void run() {
        run = true;

        try (ServerSocket listener = new ServerSocket(59090)) {
            try (Socket socket = listener.accept()) {
                InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));

                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output, true);

                String line;

                while (run) {
                    line = reader.readLine();

                    //TODO Si fa un buffer(lista) o un metodo che ascolta su richiesta(read())?
                }
            } finally {
                listener.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        run = false;
    }
}
