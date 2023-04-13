package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.LobbyList;
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
     */
    public synchronized int createLobby(String admin) {
        Lobby newLobby = new Lobby(getNewId(), admin);
        map.put(newLobby.id, newLobby);
        return newLobby.id;
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
    public synchronized LobbyList.LobbyData[] lobbiesData() {
        LobbyList.LobbyData[] data = new LobbyList.LobbyData[map.size()];
        Lobby lobby;

        for (int i = 0; i < data.length; i++) {
            lobby = get(getLobbyIds()[i]);
            data[i] = new LobbyList.LobbyData(lobby.getUsers()[0], lobby.id, lobby.getNumUsers());
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
        boolean found = false;

        for (int i = 0; i < 10; i++) {
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
}
