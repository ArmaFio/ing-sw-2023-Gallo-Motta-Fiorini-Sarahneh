package it.polimi.ingsw.messages;

public class CreateMessage extends Message {
    public final int lobbyDim;

    /**
     * Constructs a {@code CreateMessage} containing the lobby capacity.
     * Is used to send the server a request to create a lobby of the specified capacity.
     *
     * @param lobbyDim the max capacity of the lobby.
     */
    public CreateMessage(int lobbyDim) {
        super(MessageType.CREATE);
        this.lobbyDim = lobbyDim;
    }

    public int getLobbyDim() {
        return lobbyDim;
    }
}
