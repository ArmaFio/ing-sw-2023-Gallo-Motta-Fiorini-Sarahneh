package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.LobbiesList;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class LobbiesHandler {
    private final HashMap<Integer, Lobby> map = new HashMap<>();
    private final ArrayList<Integer> unAvailableIds = new ArrayList<>();

    /**
     * Creates a {@code Lobby} and adds it to the list of lobbies.
     *
     * @param admin The admin of the {@code Lobby}.
     * @return The lobby id.
     */
    public synchronized int createLobby(User admin, int lobbyDim) {
        Lobby newLobby = new Lobby(getNewId(), admin, lobbyDim, this);
        map.put(newLobby.id, newLobby);
        return newLobby.id;
    }

    /**
     * Removes the lobby with the given id.
     *
     * @param id The id of the lobby.
     */
    public synchronized void removeLobby(int id) {
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
        int sub = 0;

        for (int i = 0; i < data.length; i++) {
            lobby = get(getLobbyIds()[i]);
            if (lobby.getNumUsers() < lobby.lobbyDim) { //TODO forse vuoi farlo vedere lo stesso
                data[i + sub] = new LobbiesList.LobbyData(lobby.getAdmin(), lobby.id, lobby.getNumUsers(), lobby.lobbyDim);
            } else {
                sub--;
            }
        }
        ArrayList<LobbiesList.LobbyData> finalData = new ArrayList<>();
        for (LobbiesList.LobbyData l : data) {
            if (l != null) {
                finalData.add(l);
            }
        }
        data = finalData.toArray(new LobbiesList.LobbyData[0]);

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
    public synchronized int getNewId() {
        int n;

        for (int i = 0; i < 100; i++) {
            n = (int) (Math.random() * 1000);

            if (!unAvailableIds.contains(n) && n >= 100) {
                unAvailableIds.add(n);
                return n;
            }
        }

        Logger.error("Can't find a new lobby id");
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
            for (String user : users) {
                if (user.equals(username)) {
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
            if (map.get(id).getUsers().length == 0) {
                map.remove(id);
            }
        }
    }

    public int isActive(String username) {
        int lobby = -1;
        for (Integer l : map.keySet()) {
            for (String u : map.get(l).getUsers()) {
                if (Objects.equals(username, u)) {
                    lobby = l;
                    break;
                }
            }
        }
        return lobby;
    }

}
