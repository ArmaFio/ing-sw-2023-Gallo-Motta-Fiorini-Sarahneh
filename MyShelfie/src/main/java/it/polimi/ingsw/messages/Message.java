package it.polimi.ingsw.messages;

import java.io.Closeable;
import java.io.Serializable;

public class Message implements Serializable, Closeable {
    public final int lobbyId;
    private ResponseType type;
    private String author; //TODO passa tramite stringa e crea un metodo getUser(String) dal UsersHandler (controlla anche se il nome è valido e conserva le password)

    public Message(Message res) {
        this.type = res.type;
        this.author = res.author;
        this.lobbyId = res.lobbyId;
    }

    public Message(String author, int lobbyId) {
        this.author = author;
        this.lobbyId = lobbyId;
    }

    public Message(String author) {
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
        //TODO non toglierlo che da errore
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }
}
