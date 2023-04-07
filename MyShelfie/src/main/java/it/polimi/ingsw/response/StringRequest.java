package it.polimi.ingsw.response;

import it.polimi.ingsw.Response;

public class StringRequest extends Response {
    private String message;
    private String author;

    /**
     * Constructor for a string message class, mainly used for chat purposes.
     * @param message the message you want to send.
     */
    public StringRequest(String message, String author){
        type = ResponseType.STRING;
        this.author = author;
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
