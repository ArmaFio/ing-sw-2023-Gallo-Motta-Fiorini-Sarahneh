package it.polimi.ingsw;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.HashMap;

public class UsersHandler {
    private final HashMap<String, User> map = new HashMap<>();

    public synchronized void add(User newUser) { //TODO passa i parametri e crea te user
        if (userExists(newUser.username)) {
            map.put(newUser.username, newUser);
        } else {
            Logger.warning("Username " + newUser.username + " already exists.");
        }
    }

    public synchronized User get(String username) {
        if (map.get(username) == null) {
            Logger.warning("Username " + username + " doesn't exist.");
        }
        return map.get(username);
    }

    public void sendAll(Message message) throws IOException {
        for (String key : map.keySet()) {
            get(key).getServer().write(message);
        }
    }

    public boolean userExists(String username) {
        return map.get(username) != null;
    }

    public int size() {
        return map.size();
    }
}
