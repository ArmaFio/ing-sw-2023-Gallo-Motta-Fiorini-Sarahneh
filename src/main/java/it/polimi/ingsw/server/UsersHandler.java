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
    synchronized void add(User newUser) { //TODO passa i parametri e crea te user
        if (!userExists(newUser)) {
            map.put(newUser.getUsername(), newUser);
        } else {
            Logger.warning("Username " + newUser.getUsername() + " already exists.");
        }
    }

    /**
     * @param username The {@code username} of the {@code User}.
     * @return The {@code User} corresponding to an {@code username}.
     */
    synchronized User get(String username) {
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
    public synchronized void sendAll(Message msg) throws IOException {
        ClientHandler client;
        for (String key : map.keySet()) {
            client = get(key).getClient();
            if (client.isConnected()) {
                get(key).getClient().send(msg);
            }
        }
    }

    /**
     * @return {@code true} if the user already exists, {@code false} if not.
     * @deprecated usa contains()
     */
    @Deprecated
    public synchronized boolean userExists(User user) {
        return map.get(user.getUsername()) != null;
    }

    /**
     * @return The number of user saved.
     */
    public synchronized int size() {
        return map.size();
    }

    /**
     * @return {@code true} if the {@code User} already exists, {@code false} if not.
     */
    public synchronized boolean contains(String username) {
        return map.containsKey(username);
    }

    public synchronized HashMap<String, String> getPasswordsMap() {
        HashMap<String, String> passwords = new HashMap<>();

        for (String key : map.keySet()) {
            if (map.get(key).getUsername().charAt(0) != '/') {
                passwords.put(map.get(key).getUsername(), map.get(key).getPassword());
            }
        }

        return passwords;
    }

    public synchronized void setUsers(HashMap<String, String> passwords) {

        for (String key : passwords.keySet()) {
            add(new User(key, passwords.get(key)));
        }
    }

    public synchronized boolean setCredentials(String username, String password, ClientHandler client) {
        if (!contains(username)) {
            boolean found = false;
            Logger.debug("Adding username");

            for (String key : map.keySet()) {
                if (client.equals(get(key).getClient())) {
                    get(key).setCredentials(username, password);
                    add(get(key));
                    map.remove(key);
                    found = true;
                    break;
                }
            }

            if (!found) {
                Logger.error("ClientHandler not found");
                return false;
            }

            LoadSave.write(MainServer.PASSWORDS_PATH, getPasswordsMap());
            return true;
        } else if (contains(username) && get(username).checkPassword(password) && !get(username).isConnected()) {
            boolean found = false;
            for (String key : map.keySet()) { //TODO duplicato
                if (client.equals(get(key).getClient())) {
                    map.remove(key);
                    found = true;
                    break;
                }
            }
            get(username).setClient(client);

            if (!found) {
                Logger.error("ClientHandler not found");
                return false;
            }

            LoadSave.write(MainServer.PASSWORDS_PATH, getPasswordsMap());
            return true;
        }

        return false;
    }
}
