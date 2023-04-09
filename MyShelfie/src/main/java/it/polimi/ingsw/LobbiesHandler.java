package it.polimi.ingsw;

import it.polimi.ingsw.server.ClientHandler;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbiesHandler {
    private final HashMap<Integer, Lobby> map = new HashMap<>();
    private final ArrayList<Integer> availableIds = new ArrayList<>();

    public synchronized int createLobby(String admin, ClientHandler server) {
        Lobby newLobby = new Lobby(admin, server);
        map.put(newLobby.id, newLobby);
        return newLobby.id;
    }

    public synchronized Lobby get(int i) {

        if (map.get(i) == null) {
            Logger.warning("Lobby " + i + " doesn't exist.");
        }
        return map.get(i);
    }

    public int[] lobbiesCapacity() {
        int[] lobbiesDim = new int[map.size()];

        for (int i = 0; i < lobbiesDim.length; i++) {
            lobbiesDim[i] = map.get(i).getNumUsers();
        }

        return lobbiesDim;
    }

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

    public int size() {
        return map.size();
    }
}
