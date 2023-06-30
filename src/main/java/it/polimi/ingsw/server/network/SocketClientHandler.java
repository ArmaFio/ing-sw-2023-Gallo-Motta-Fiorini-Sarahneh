package it.polimi.ingsw.server.network;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.AlreadyBoundException;

public class SocketClientHandler extends ClientHandler {
    final String userAddress;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    /**
     * ClientHandler constructor, handles the connection with the client until a new game is created or the client decides to join a game.
     *
     * @param id       thread id, only visible in the server.
     * @param listener client socket.
     */
    public SocketClientHandler(SocketMainServer server, int id, Socket listener) throws IOException, AlreadyBoundException {
        super(server, id);
        connect();
        this.userAddress = listener.getRemoteSocketAddress().toString();
        this.outputStream = new ObjectOutputStream(listener.getOutputStream());
        this.inputStream = new ObjectInputStream(listener.getInputStream());
        Logger.info("The thread " + id + " is now connected with the player ip " + userAddress);
        this.start();
    }

    public SocketClientHandler() {
        super();
        this.userAddress = null;
        this.outputStream = null;
        this.inputStream = null;
    }


    /**
     * Reads a serialized object received from the client.
     *
     * @return the object read.
     */
    Message read() throws IOException, ClassNotFoundException {
        Message msg;

        if (this.isConnected()) {
            msg = (Message) inputStream.readObject();
            Logger.info("Message " + msg.getType().toString() + " received by " + userAddress + "(" + username + ")");
        } else {
            throw new IOException();
        }

        return msg;
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param msg the {@code Message} we want to send to the client.
     */
    public void send(Message msg) throws IOException {
        if (this.isConnected()) {
            msg.setAuthor(this.username);
            this.outputStream.writeObject(msg);
            Logger.info("Message " + msg.getType().toString() + " sent to " + userAddress + "(" + username + ")");
        }
    }

    /**
     * Sets the {@code isConnected} state of the user to false and disconnects the user from the lobby.
     */

    @Override
    public String getAddress() {
        return userAddress;
    }


}
