package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.utils.LoadSave;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MainServer {
    public static final String PASSWORDS_PATH = "MyShelfie/src/main/java/it/polimi/ingsw/server/Accounts.ser";
    final UsersHandler users = new UsersHandler();
    final LobbiesHandler lobbies = new LobbiesHandler();

    public MainServer() throws IOException, InterruptedException {
        int threadCount = 0;
        Socket s = null;
        ServerSocket ss = null;
        Logger.info("New execution");

        //LoadSave.write(PASSWORDS_PATH, new HashMap<String, String>());
        try {
            HashMap<String, String> usersPassword = (HashMap<String, String>) LoadSave.read(PASSWORDS_PATH);
            if (usersPassword.size() > 0) {
                Logger.debug("Trovato file password contenente:");
                for (String key : usersPassword.keySet()) {
                    Logger.debug(key + " " + usersPassword.get(key));
                }
                users.setUsers(usersPassword);
            } else {
                Logger.debug("Creating password file!");
            }
        } catch (RuntimeException e) {
            Logger.error("An error occurred! " + e);
        }

        Logger.info("Main server listening...");
        try {
            ss = new ServerSocket(59090);
        } catch (IOException e) {
            Logger.error("Failed in creating a socket.");
        }
        while (true) {
            try {
                s = ss.accept();

                Logger.debug("Users saved before this new connection:");
                for (String key : users.getPasswordsMap().keySet()) {
                    Logger.debug(key + " " + users.getPasswordsMap().get(key));
                }

                ClientHandler t = new ClientHandler(this, threadCount, s);

                threadCount++;
            } catch (IOException e) {
                Logger.warning("Accept failure." + e);
            }
        }

        //MainServer ms = new MainServer();
        //ms.start();
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
     * @param msg The message to send.
     */
    public void sendToLobby(int lobbyId, Message msg) throws IOException {
        for (String key : lobbies.get(lobbyId).getUsers()) {
            users.get(key).getServer().write(msg);
        }
    }
}
