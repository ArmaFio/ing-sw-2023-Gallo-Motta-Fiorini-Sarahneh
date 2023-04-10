package it.polimi.ingsw.messages;

@Deprecated
public class LoginOutcome extends Message {
    private String outcome;
    private String message;

    public LoginOutcome(String outcome, String message) {
        super(ResponseType.LOGIN_OUTCOME);
        this.outcome = outcome;
        this.message = message;
    }

    /**
     * @return the outcome of the login request.
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * @return the message written by the author.
     */
    public String getMessage() {
        return message;
    }
}
