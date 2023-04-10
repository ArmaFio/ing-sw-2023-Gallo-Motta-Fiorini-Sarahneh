package it.polimi.ingsw;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.HashMap;

public class UsersHandler {
    private final HashMap<String, User> map = new HashMap<>();

    /**
     * Adds a {@code User} to the list of lobbies.
     *
     * @param newUser The {@code User} to add.
     */
    public synchronized void add(User newUser) { //TODO passa i parametri e crea te user
        if (!userExists(newUser.username)) {
            map.put(newUser.username, newUser);
        } else {
            Logger.warning("Username " + newUser.username + " already exists.");
        }
    }

    /**
     * @param username The {@code username} of the {@code User}.
     * @return The {@code User} corresponding to an {@code username}.
     */
    public synchronized User get(String username) {
        if (!contains(username)) {
            Logger.warning("Username " + username + " doesn't exist.");
        }
        return map.get(username);
    }

    /**
     * Sends to all users a message.
     *
     * @param msg The message to send.
     */
    public void sendAll(Message msg) throws IOException {
        for (String key : map.keySet()) {
            get(key).getServer().write(msg);
        }
    }

    /**
     * @return {@code true} if the user already exists, {@code false} if not.
     * @deprecated usa contains()
     */
    @Deprecated
    public boolean userExists(String username) {
        return map.get(username) != null;
    }

    /**
     * @return The number of user saved.
     */
    public int size() {
        return map.size();
    }

    /**
     * @return {@code true} if the {@code User} already exists, {@code false} if not.
     */
    public boolean contains(String author) {
        return map.containsKey(author);
    }
}
