package it.polimi.ingsw.response;

import it.polimi.ingsw.Response;

public class LoginRequest extends Response {
    private String author;

    public LoginRequest(String author) {
        this.type = ResponseType.LOGIN_REQUEST;
        this.author = author;
    }
}
