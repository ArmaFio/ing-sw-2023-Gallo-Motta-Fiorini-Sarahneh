package it.polimi.ingsw.messages;

public class LobbyList extends Message {
    public final int[] lobbiesDim; //TODO anche id

    public LobbyList(String author, int[] lobbiesDim) {
        super(author);
        setType(ResponseType.LOBBY_LIST);
        this.lobbiesDim = lobbiesDim;
    }
}
