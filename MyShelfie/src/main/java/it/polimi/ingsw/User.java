package it.polimi.ingsw;

import it.polimi.ingsw.server.ClientHandler;

public class User {
    public final String username;
    private final String password;
    private int lobbyId;
    private ClientHandler server;

    public User(String username, String password, ClientHandler server) {
        this.username = username;
        this.password = password;
        this.lobbyId = -1;
        this.server = server;
    }

    private User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.lobbyId = user.getLobbyId();
        this.server = user.getServer();
    }

    @Deprecated
    public User(ClientHandler server) {
        this("None", "None", server);
    }

    public ClientHandler getServer() {
        return server;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setServer(ClientHandler server) {
        this.server = server;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    @Deprecated
    public boolean isNone() {
        return username.equals("None");
    }

    public User copy() {
        return new User(this);
    }

    @Override
    public String toString() {
        return username;
    }

    public void setLobbyId(int lobby) {
        this.lobbyId = lobby;
    }
}
