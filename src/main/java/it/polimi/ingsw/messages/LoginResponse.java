package it.polimi.ingsw.messages;

public class LoginResponse extends Message {
    private final String username;
    private final String password;

    public LoginResponse(String username, String password) {
        super(MessageType.LOGIN_RESPONSE);
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username chosen by the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password chosen by the client.
     */
    public String getPassword() {
        return password;
    }
}
