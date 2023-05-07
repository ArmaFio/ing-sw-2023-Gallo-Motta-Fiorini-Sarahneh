package it.polimi.ingsw.messages;


public class StringRequest extends Message { //Usa solo per emssaggi di errore
    private final String message;

    /**
     * Constructor for a string message class, mainly used for chat purposes.
     *
     * @param message the message you want to send.
     */
    public StringRequest(String message) { //TODO author viene assegnato automaticamente
        super(MessageType.STRING);
        this.message = message;
    }

    /**
     * @return the message in this object.
     */
    public String message() {
        return "[" + getAuthor() + "] " + message;
    }

}
