package it.polimi.ingsw.messages;

import java.io.Closeable;
import java.io.Serializable;

public class Message implements Serializable, Closeable {
    public final int lobbyId;
    private MessageType type;
    private String author; //TODO passa tramite stringa e crea un metodo getUser(String) dal UsersHandler (controlla anche se il nome è valido e conserva le password)

    public Message(MessageType type, String author, int lobbyId) {
        this.type = type;
        this.author = author;
        this.lobbyId = lobbyId;
    }

    public Message(Message res) {
        this.type = res.type;
        this.author = res.author;
        this.lobbyId = res.lobbyId;
    }

    public Message(MessageType type) {
        this(type, "None", -1);
    }

    public Message(MessageType type, int lobbyId) {
        this(type, "None", lobbyId);
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public void close() {
        //TODO non toglierlo che da errore
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
