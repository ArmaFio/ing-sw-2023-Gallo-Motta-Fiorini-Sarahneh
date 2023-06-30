package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Chat;
import it.polimi.ingsw.messages.ChatMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.network.ClientHandler;
import it.polimi.ingsw.server.network.SocketClientHandler;

import java.io.IOException;
import java.util.ArrayList;

public class User {
    protected ArrayList<ChatMessage> chat;
    private String username;
    private String password;
    private int lobbyId;
    private ClientHandler clientHandler;

    public User(String username, String password, ClientHandler client) {
        this.username = username;
        this.password = password;
        this.lobbyId = -1;
        this.clientHandler = client;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.lobbyId = -1;
        this.clientHandler = new SocketClientHandler();
    }

    public User(ClientHandler client) {
        this(client.getAddress(), "None", client);
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

    @Override
    public String toString() {
        return username;
    }

    /**
     * @return The connection state of the user.
     */
    public boolean isConnected() {
        return clientHandler.isConnected();
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

    public void sendChat() throws IOException {
        ChatMessage[] arrayChat = chat.toArray(ChatMessage[]::new);
        send(new Chat(arrayChat));
    }

}
