package it.polimi.ingsw.messages;

public class LobbyJoined extends Message {
    private final String[] lobbyUsers;

    public LobbyJoined(String author, int idLobby, String[] lobbyUsers) {
        super(author, idLobby);
        super.setType(ResponseType.LOBBY_JOINED);
        this.lobbyUsers = lobbyUsers;
    }

    public String[] getLobbyUsers() {
        String[] r = new String[lobbyUsers.length];

        for (int i = 0; i < r.length; i++) {
            r[i] = lobbyUsers[i];
        }

        return r;
    }
}
