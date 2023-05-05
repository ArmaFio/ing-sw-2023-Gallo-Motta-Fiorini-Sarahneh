package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.LoadSave;
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
        if (!userExists(newUser)) {
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
        ClientHandler client;
        for (String key : map.keySet()) {
            client = get(key).getClient();
            if (client != null) {
                get(key).getClient().send(msg);
            }
        }
    }

    /**
     * @return {@code true} if the user already exists, {@code false} if not.
     * @deprecated usa contains()
     */
    @Deprecated
    public boolean userExists(User user) {
        return map.get(user.username) != null;
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
    public boolean contains(String username) {
        return map.containsKey(username);
    }

    public HashMap<String, String> getPasswordsMap() {
        HashMap<String, String> passwords = new HashMap<>();

        for (String key : map.keySet()) {
            passwords.put(map.get(key).username, map.get(key).password);
        }

        return passwords;
    }

    public void setUsers(HashMap<String, String> passwords) {

        for (String key : passwords.keySet()) {
            add(new User(key, passwords.get(key)));
        }
    }

    public boolean setCredentials(String username, String password, ClientHandler client) {
        if (!contains(username)) {
            boolean found = false;
            Logger.debug("Adding username");

            for (String key : map.keySet()) {
                if (get(key).getClient().equals(client)) {
                    get(key).setCredentials(username, password);
                    found = true;
                    break;
                }
            }

            if (!found) {
                Logger.error("ClientHandler not found");
            }

            LoadSave.write(MainServer.PASSWORDS_PATH, getPasswordsMap());
            return true;
        } else if (contains(username) && get(username).checkPassword(password) && !get(username).isConnected()) {
            get(username).setClient(client);
            get(username).setConnected(true);

            LoadSave.write(MainServer.PASSWORDS_PATH, getPasswordsMap());
            return true;
        }

        return false;
    }
}
