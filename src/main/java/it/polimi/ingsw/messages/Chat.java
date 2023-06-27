package it.polimi.ingsw.messages;

public class Chat extends Message {
    ChatMessage[] messages;

    public Chat(ChatMessage[] messages) {
        super(MessageType.CHAT);
        this.messages = messages;
    }

    public ChatMessage[] getMessages() {
        return messages;
    }
}
