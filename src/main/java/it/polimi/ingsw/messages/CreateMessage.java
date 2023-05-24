package it.polimi.ingsw.messages;

public class CreateMessage extends Message {
    public final int lobbyDim;

    public CreateMessage(int lobbyDim) {
        super(MessageType.CREATE);
        this.lobbyDim = lobbyDim;
    }

    public int getLobbyDim() {
        return lobbyDim;
    }
}
