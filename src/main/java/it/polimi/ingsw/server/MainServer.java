package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.LoadSave;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

public class MainServer {
    static final String PASSWORDS_PATH = "./src/main/java/it/polimi/ingsw/server/Accounts.ser";
    final UsersHandler users = new UsersHandler(); //TODO forse private
    final LobbiesHandler lobbies = new LobbiesHandler();
    private int threadCount = 0;
    private final MainServerRMInterfaceImpl rmiServer;
    private boolean running = true;


    public MainServer() throws IOException, InterruptedException, AlreadyBoundException {//TODO try and catch
        Socket socket;
        ServerSocket serverSocket = null;
        Logger.info("New execution");

        //LoadSave.write(PASSWORDS_PATH, new HashMap<String, String>());
        HashMap<String, String> usersPassword = loadPasswords();
        users.setUsers(usersPassword);
        rmiServer = new MainServerRMInterfaceImpl(this);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("MainServer", rmiServer);

        Logger.info("Main server listening...");
        try {
            serverSocket = new ServerSocket(59090);
        } catch (IOException e) {
            Logger.error("Failed in creating a socket.");
        }


        if (serverSocket != null) {
            while (running) {
                try {
                    socket = serverSocket.accept();

                    int i = getThreadCount();
                    SocketClientHandler client = new SocketClientHandler(this, i, socket);

                    users.add(new User(client));

                } catch (IOException e) {
                    Logger.error("Accept failure." + e);
                    running = false;
                }
            }
        }
    }


    /**
     * Reads the passwords file and return the {@code HashMap} containing them.
     *
     * @return The {@code HashMap} containing the passwords.
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, String> loadPasswords() {
        HashMap<String, String> usersPassword = new HashMap<>(0);

        try {
            usersPassword = (HashMap<String, String>) LoadSave.read(PASSWORDS_PATH);
            if (usersPassword.size() == 0) {
                Logger.info("Creating password file!");
            }
        } catch (RuntimeException e) {
            Logger.error("An error occurred! " + e);
        }

        return usersPassword;
    }

    /**
     * Method that sends to all the users a message.
     *
     * @param msg The message to send.
     */
    public void sendAll(Message msg) throws IOException {
        users.sendAll(msg);
    }

    /**
     * Method that sends to all the users in the {@code Lobby} a message.
     *
     * @param lobbyId The lobby id.
     * @param msg     The message to send.
     */
    public void sendToLobby(int lobbyId, Message msg) throws IOException {
        lobbies.get(lobbyId).sendToLobby(msg);
    }

    public Lobby getLobby(int id) {
        return lobbies.get(id);
    }

    public Lobby getLobby(String user) {
        return lobbies.get(users.get(user).getLobbyId());
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public boolean setCredentials(String username, String password, ClientHandler idClient) {
        return users.setCredentials(username, password, idClient);
    }

    public synchronized int getThreadCount() {
        int i = threadCount;
        threadCount++;
        return i;
    }
}
