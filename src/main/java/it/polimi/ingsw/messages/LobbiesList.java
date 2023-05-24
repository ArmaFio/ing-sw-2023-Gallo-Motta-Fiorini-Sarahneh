package it.polimi.ingsw.messages;

import java.io.Serializable;

public class LobbiesList extends Message {
    public final LobbyData[] lobbiesData; //TODO anche id
    public final boolean update;

    public LobbiesList(LobbyData[] lobbiesData, boolean update) {
        super(MessageType.LOBBIES_LIST);
        this.lobbiesData = lobbiesData;
        this.update = update;
    }

    public static class LobbyData implements Serializable { //TODO forse in un file a parte
        public final String admin;
        public final int id;
        public final int capacity;

        public final int lobbyDim;

        public LobbyData(String admin, int id, int capacity, int lobbyDim) {
            this.admin = admin;
            this.id = id;
            this.capacity = capacity;
            this.lobbyDim = lobbyDim;
        }
    }
}
