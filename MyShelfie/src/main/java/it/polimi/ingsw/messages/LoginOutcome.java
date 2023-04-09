package it.polimi.ingsw.messages;

public class LoginOutcome extends Message {
    private String author;
    private String outcome;
    private String message;

    public LoginOutcome(String username, String outcome, String message) {
        this.type = ResponseType.LOGIN_OUTCOME;
        this.author = username;
        this.outcome = outcome;
        this.message = message;
    }

    /**
     * @return the author of the message.
     */
    public String getAuthor() {
        return author;
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
