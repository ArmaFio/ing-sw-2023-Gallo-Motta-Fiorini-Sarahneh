package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.Logger;

public class SocketNetworkHandler extends NetworkHandler {
    private final String serverIp;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;


    public SocketNetworkHandler(int choice, String ip) throws IOException {
        super(choice);

        serverIp = ip;
        connect();
        //start listening for server instructions

        clientStart();
    }

    /**
     * Creates a connection between client and server, keeps trying until success.
     */
    void connect() {
        boolean connected = false;
        boolean firstTime = true;
        while (!connected) {
            try {
                socket = new Socket(serverIp, 59090);
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                outputStream = new ObjectOutputStream(output);
                inputStream = new ObjectInputStream(input);
                firstTime = true;
                connected = true;
                Logger.info("Connection Established!");
            } catch (IOException e) {
                if (firstTime) {
                    Logger.warning("Cannot connect to the server, keep trying...");
                    firstTime = false;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException i) {
                    Logger.error("InterruptedException occurred!");
                }
            }
        }
    }


    /**
     * Reads a serialized object received from the client.
     *
     * @return the object read.
     */
    @Override
    public Message read() {
        Message msg;
        try {
            msg = (Message) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        Logger.info("Message " + msg.getType().toString() + " received");
        return msg;
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param obj the object we want to send to the client.
     */
    @Override
    public void write(Message obj) {
        obj.setAuthor(username);
        try {
            outputStream.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        view.setUsername(username);
    }

    @Override
    public void disconnect() throws IOException {
        view.updateState(GameState.CLOSE);
        running = false;
        this.socket.close();
        view.disconnect();
    }
}
