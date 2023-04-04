package it.polimi.ingsw;

public class User {
    public final String username;
    private int gameId;

    public User(String username) {
        this.username = username;
        gameId = -1;
    }

    public User() {
        this("None");
    }

    public int getGameId() {
        return gameId;
    }

    public boolean check_if_available(String username) {
        //TODO da fare
        return true;
    }

    public boolean isNone() {
        return username.equals("None");
    }

    @Override
    public String toString() {
        return username;
    }
}
