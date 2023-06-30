package it.polimi.ingsw.messages;

import java.io.Closeable;
import java.io.Serializable;
import java.rmi.Remote;

public class Message implements Serializable, Closeable, Remote {
    public final int lobbyId;
    private MessageType type;
    private String author;

    /**
     * Constructs a generic message.
     *
     * @param type    type of the message.
     * @param author  author of the message.
     * @param lobbyId id of sender's lobby.
     */
    public Message(MessageType type, String author, int lobbyId) {
        this.type = type;
        this.author = author;
        this.lobbyId = lobbyId;
    }

    /**
     * Constructs a generic message.
     *
     * @param res message containing information to copy.
     */
    public Message(Message res) {
        this.type = res.type;
        this.author = res.author;
        this.lobbyId = res.lobbyId;
    }

    /**
     * Constructs a generic message.
     *
     * @param type type of the message.
     */
    public Message(MessageType type) {
        this(type, "None", -1);
    }

    /**
     * Constructs a generic message.
     *
     * @param type    type of the message.
     * @param lobbyId id of the lobby.
     */
    public Message(MessageType type, int lobbyId) {
        this(type, "None", lobbyId);
    }

    /**
     * Constructs a generic message.
     *
     * @param type   type of the message.
     * @param author author of the message.
     */
    public Message(MessageType type, String author) {
        this.type = type;
        this.author = author;
        this.lobbyId = -1;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void close() {

    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
