package it.polimi.ingsw.messages;

public class JoinSuccess extends Message {
    private final String[] lobbyUsers;

    public JoinSuccess(int idLobby, String[] lobbyUsers) {
        super(idLobby);
        super.setType(ResponseType.JOIN_SUCCESS);
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
