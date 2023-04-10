package it.polimi.ingsw;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.server.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;

public class Lobby extends Thread {
    public final int id;
    private ArrayList<String> users; //TODO salva solo i nomi, usa UsersHandler se ti servono dati
    private ClientHandler server;
    private boolean isGameStarted;
    private Controller gameController;

    public Lobby(String admin, ClientHandler server) {
        this.id = ClientHandler.lobbies.getNewId();
        users = new ArrayList<>();
        users.add(admin);
        this.server = server;
        isGameStarted = false;
        users = new ArrayList<>();
        this.start();
    }


    @Override
    public void run() {
        while (!isGameStarted) {
            try {
                this.wait(); //TODO da errore current thread is not owner
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        String[] arr = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            arr[i] = users.get(i);
        }
        gameController = new Controller(server, arr);

    }

    public void startGame() {
        isGameStarted = true;
        notifyAll();
    }

    public boolean addUser(String user) {
        if (users.size() < 4 && !ClientHandler.users.contains(user)) {
            users.add(user);
            return true;
        }
        return false;
    }

    public boolean removeUser(String user) { //TODO fai gestire da LobbiesHandler così cancella la lobby se finiscono gli utenti
        if (ClientHandler.users.contains(user)) {
            users.remove(user);
            return true;
        }
        return false;
    }

    public void sendAll(Message message) throws IOException {
        for (String key : users) {
            ClientHandler.users.get(key).getServer().write(message);
        }

    }

    public String[] getUsers() {
        String[] r = new String[users.size()];

        for (int i = 0; i < r.length; i++) {
            r[i] = users.get(i);
        }

        return r;
    }

    public int getNumUsers() {
        return users.size();
    }
}
