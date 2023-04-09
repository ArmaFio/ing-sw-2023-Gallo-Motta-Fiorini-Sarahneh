package it.polimi.ingsw;

import it.polimi.ingsw.server.ClientHandler;

public class User {
    public final String username;
    private int gameId;
    private ClientHandler server;

    public User(String username, ClientHandler server) {
        this.username = username;
        this.gameId = -1;
        this.server = server;
    }

    private User(User user) {
        this.username = user.username;
        this.gameId = user.getGameId();
        this.server = user.getServer();
    }

    @Deprecated
    public User(ClientHandler server) {
        this("None", server);
    }

    public ClientHandler getServer() {
        return server;
    }

    public void setServer(ClientHandler server) {
        this.server = server;
    }

    public int getGameId() {
        return gameId;
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
}
