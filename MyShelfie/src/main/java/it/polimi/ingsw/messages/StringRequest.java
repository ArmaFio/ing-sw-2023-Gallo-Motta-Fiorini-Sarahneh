package it.polimi.ingsw.messages;

public class StringRequest extends Message {
    private String message;
    private String author;

    /**
     * Constructor for a string message class, mainly used for chat purposes.
     *
     * @param message the message you want to send.
     */
    public StringRequest(String message, String author) {
        super(author);
        setType(ResponseType.STRING);
        this.message = message;
    }

    /**
     *
     * @return the message in this object.
     */
    public String message(){
        return message;
    }

    /**
     *
     * @return the author of this message.
     */
    public String user(){
        return author;
    }
}
