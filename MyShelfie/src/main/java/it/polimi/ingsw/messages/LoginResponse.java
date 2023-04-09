package it.polimi.ingsw.messages;

public class LoginResponse extends Message {
    private final String password;

    public LoginResponse(String username, String password) {
        super(username);
        setType(ResponseType.LOGIN_RESPONSE);
        this.password = password;
    }

    /**
     * @return the password chosen by the client.
     */
    public String getPassword() {
        return password;
    }
}
