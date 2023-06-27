package it.polimi.ingsw.messages;

public class ChatMessage extends Message {
    private final String message;
    private final String receiver;

    public ChatMessage(String message) {
        super(MessageType.CHAT_MESSAGE);
        this.message = message;
        this.receiver = "";
    }

    public ChatMessage(String receiver, String message) {
        super(MessageType.CHAT_MESSAGE);
        this.message = message;
        this.receiver = receiver;
    }


    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }
}
