package it.polimi.ingsw.messages;

public class JoinRequest extends Message {
    public JoinRequest(String s) {
        toObject(s);
    }

    @Override
    public String toString() {
        return "Join={" +
                "Author" + ':' + author.username +
                '}';
    }
}
