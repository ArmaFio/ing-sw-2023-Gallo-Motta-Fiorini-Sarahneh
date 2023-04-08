package it.polimi.ingsw.response;

import it.polimi.ingsw.Response;

public class LoginResponse extends Response {
    private String author;
    private String password;

    public LoginResponse(String username, String password) {
        this.type = ResponseType.LOGIN_RESPONSE;
        this.author = username;
        this.password = password;
    }

    /**
     * @return the username chosen by the client.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the password chosen by the client.
     */
    public String getPassword() {
        return password;
    }
}
