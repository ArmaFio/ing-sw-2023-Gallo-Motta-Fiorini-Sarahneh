package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.LobbiesList;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbiesHandler {
    private final HashMap<Integer, Lobby> map = new HashMap<>();
    private final ArrayList<Integer> availableIds = new ArrayList<>();

    /**
     * Creates a {@code Lobby} and adds it to the list of lobbies.
     *
     * @param admin The admin of the {@code Lobby}.
     * @return The lobby id.
     */
    public synchronized int createLobby(User admin) {
        Lobby newLobby = new Lobby(getNewId(), admin);
        map.put(newLobby.id, newLobby);
        return newLobby.id;
    }

    /**
     * Removes the lobby with the given id.
     *
     * @param id The id of the lobby.
     */
    private synchronized void removeLobby(int id) {
        map.remove(id);
    }

    /**
     * @param id The {@code id} of the {@code Lobby}.
     * @return The {@code Lobby} corresponding to an {@code id}.
     */
    public synchronized Lobby get(int id) {
        if (!contains(id)) {
            Logger.warning("Lobby " + id + " doesn't exist.");
        }
        return map.get(id);
    }

    /**
     * @return {@code true} if the {@code Lobby} already exists, {@code false} if not.
     */
    public boolean contains(int id) {
        return map.containsKey(id);
    }

    /**
     * Gives the capacity of the lobbies.
     *
     * @return An array containing the capacity of all the lobbies.
     */
    public synchronized LobbiesList.LobbyData[] lobbiesData() {
        LobbiesList.LobbyData[] data = new LobbiesList.LobbyData[map.size()];
        Lobby lobby;

        for (int i = 0; i < data.length; i++) {
            lobby = get(getLobbyIds()[i]);
            data[i] = new LobbiesList.LobbyData(lobby.getUsers()[0], lobby.id, lobby.getNumUsers());
        }

        return data;
    }

    /**
     * Gives the ids of the lobbies.
     *
     * @return An array containing the ids of all the lobbies.
     */
    public synchronized int[] getLobbyIds() {
        ArrayList<Integer> keySet = new ArrayList<>(map.keySet());
        int[] result = new int[keySet.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = keySet.get(i);
        }

        return result;
    }

    /**
     * Generates a new id for a {@code Lobby}.
     *
     * @return The new id generated.
     */
    public synchronized int getNewId() { //TODO cancella id quando la lobby termina.
        boolean found;

        for (int i = 0; i < 10; i++) {
            found = false;
            for (int id : availableIds) {
                if (i == id) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                availableIds.add(i);
                return i;
            }
        }

        Logger.warning("Can't find a new lobby id"); //TODO throw an error
        return -1;
    }

    /**
     * @return The number of lobbies.
     */
    public int size() {
        return map.size();
    }

    /**
     * Removes the user from his lobby, if there are no longer users in the lobby, deletes the lobby.
     *
     * @param username The username of the player we want to remove from the lobby.
     */
    public void removeUser(String username) {
        int id = -1;
        boolean found = false;
        String[] users;
        for (int key : map.keySet()) {
            users = map.get(key).getUsers();
            for (int i = 0; i < users.length; i++) {
                if (users[i].equals(username)) {
                    id = key;
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        if (id != -1) {
            map.get(id).removeUser(username);
        }

        if (map.get(id).getUsers().length == 0) {
            removeLobby(id);
        }
    }
}
