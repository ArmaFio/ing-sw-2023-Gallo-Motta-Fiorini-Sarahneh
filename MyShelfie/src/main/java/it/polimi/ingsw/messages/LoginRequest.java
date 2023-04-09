package it.polimi.ingsw.messages;

public class LoginRequest extends Message {
    private String author;

    public LoginRequest(String author) {
        this.type = ResponseType.LOGIN_REQUEST;
        this.author = author;
    }
}
