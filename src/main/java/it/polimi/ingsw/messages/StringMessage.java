package it.polimi.ingsw.messages;


public class StringMessage extends Message {
    private final String message;

    /**
     * Constructor for a string message class, mainly used for chat purposes.
     *
     * @param message the message you want to send.
     */
    public StringMessage(String message) { //TODO author viene assegnato automaticamente
        super(MessageType.STRING);
        this.message = message;
    }

    /**
     * @return the message in this object.
     */
    public String message() {
        return message;
    }

}
