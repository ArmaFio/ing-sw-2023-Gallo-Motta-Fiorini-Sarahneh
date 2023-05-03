package it.polimi.ingsw.messages;

public class JoinSuccess extends Message {
    private final String[] lobbyUsers;

    public JoinSuccess(int idLobby, String[] lobbyUsers) {
        super(idLobby);
        super.setType(MessageType.JOIN_SUCCESS);
        this.lobbyUsers = lobbyUsers;
    }

    public String[] getLobbyUsers() {
        String[] r = new String[lobbyUsers.length];

        System.arraycopy(lobbyUsers, 0, r, 0, r.length);

        return r;
    }
}
