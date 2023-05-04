package it.polimi.ingsw.messages;

import java.io.Serializable;

public class LobbyList extends Message {
    public final LobbyData[] lobbiesData; //TODO anche id
    public final boolean update;

    public LobbyList(LobbyData[] lobbiesData, boolean update) {
        super(MessageType.LOBBY_LIST);
        this.lobbiesData = lobbiesData;
        this.update = update;
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
