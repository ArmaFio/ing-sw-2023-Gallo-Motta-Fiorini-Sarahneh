package it.polimi.ingsw.messages;

@Deprecated
public class StringRequest extends Message { //Usa solo per emssaggi di errore
    private final String message;
    private final String author;

    /**
     * Constructor for a string message class, mainly used for chat purposes.
     *
     * @param message the message you want to send.
     */
    public StringRequest(String message, String author) {
        super(author);
        setType(MessageType.STRING);
        this.message = message;
        this.author = author;
    }

    /**
     *
     * @return the message in this object.
     */
    public String message(){
        return "[" + author + "] " + message;
    }

}
