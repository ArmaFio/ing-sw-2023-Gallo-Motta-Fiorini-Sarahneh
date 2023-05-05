package it.polimi.ingsw.messages;

public class LobbyData extends Message {
    private final String[] lobbyUsers;


    public LobbyData(int idLobby, String[] lobbyUsers) {
        super(MessageType.JOIN_SUCCESS, idLobby);
        this.lobbyUsers = lobbyUsers;
    }


    public String[] getLobbyUsers() {
        return lobbyUsers;
    }
}
