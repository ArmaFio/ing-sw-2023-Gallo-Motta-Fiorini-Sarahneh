package it.polimi.ingsw.messages;

public class LobbyList extends Message {
    public final int[] lobbiesDim; //TODO anche id

    public LobbyList(int[] lobbiesDim) {
        super(ResponseType.LOBBY_LIST);
        this.lobbiesDim = lobbiesDim;
    }
}
