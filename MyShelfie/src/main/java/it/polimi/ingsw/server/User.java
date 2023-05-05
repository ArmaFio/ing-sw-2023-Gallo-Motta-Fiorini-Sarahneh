package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;

import java.io.IOException;

public class User {
    private String username;
    private String password; //TODO metti User insieme a UserHandler in un package
    private int lobbyId;
    private ClientHandler clientHandler;
    private boolean connected = false;

    public User(String username, String password, ClientHandler client) {
        this.username = username;
        this.password = password;
        this.lobbyId = -1;
        this.clientHandler = client;
        connected = true;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.lobbyId = -1;
        this.clientHandler = new ClientHandler();
    }

    private User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.lobbyId = user.getLobbyId();
        this.clientHandler = user.getClient();
        this.connected = user.connected;
    }

    public User(ClientHandler client) {
        this(client.userAddress, "None", client);
    }

    public ClientHandler getClient() {
        return clientHandler;
    }

    void setClient(ClientHandler client) {
        this.clientHandler = client;
    }

    public String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobby) {
        this.lobbyId = lobby;
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

    /**
     * @return The connection state of the user.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sets the connection state of the user.
     *
     * @param connected The state we want to set.
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void send(Message msg) throws IOException {
        clientHandler.send(msg);
    }

    public boolean equals(String username) {
        return this.username.equals(username);
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
