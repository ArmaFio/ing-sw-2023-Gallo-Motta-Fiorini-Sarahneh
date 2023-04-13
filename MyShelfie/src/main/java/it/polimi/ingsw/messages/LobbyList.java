package it.polimi.ingsw.messages;

import java.io.Serializable;

public class LobbyList extends Message {
    public final LobbyData[] lobbiesData; //TODO anche id

    public LobbyList(LobbyData[] lobbiesData) {
        super(ResponseType.LOBBY_LIST);
        this.lobbiesData = lobbiesData;
    }

    public static class LobbyData implements Serializable { //TODO forse in un file a parte
        public final String admin;
        public final int id;
        public final int capacity;

        public LobbyData(String admin, int id, int capacity) {
            this.admin = admin;
            this.id = id;
            this.capacity = capacity;
        }
    }
}
