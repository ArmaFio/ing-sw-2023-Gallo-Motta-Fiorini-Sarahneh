package it.polimi.ingsw.messages;

public class Chat extends Message {
    String[] messages;

    public Chat(String[] messages) {
        super(MessageType.CHAT);
        this.messages = messages;
    }

    public String[] getMessages() {
        return messages;
    }
}
