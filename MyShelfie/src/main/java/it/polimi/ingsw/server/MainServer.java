package it.polimi.ingsw.server;

import it.polimi.ingsw.LobbiesHandler;
import it.polimi.ingsw.UsersHandler;
import it.polimi.ingsw.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class MainServer {
    @Deprecated
    private static UsersHandler users;
    @Deprecated
    private static LobbiesHandler lobbies;

    public static void main(String[] args) throws IOException, InterruptedException {
        int threadCount = 0;
        Object syn = new Object();
        HashMap<String, String> usersPassword = new HashMap<>(); //contains the ip associated to the username(the key is the ip) non serve più
        File accounts = null;
        Socket s = null;
        ServerSocket ss = null;
        /*
        try {
            accounts = new File("MyShelfie/src/main/java/it/polimi/ingsw/server/Accounts.txt");
            if (!accounts.createNewFile()) {
                usersPassword = (HashMap<String, String>) LoadSave.read(accounts.getPath());
                System.out.println("Trovato file accounts contenente:");
                for (String key : usersPassword.keySet()) {
                    System.out.println(key + " " + usersPassword.get(key));
                }
            }
        } catch (RuntimeException e) {
            System.out.println("An error occurred!");
        }
         */
        Logger.info("Main server listening...");
        try {
            ss = new ServerSocket(59090);
        } catch (IOException e) {
            Logger.error("Failed in creating a socket.");
        }
        while (true) {
            try {
                s = ss.accept();
                Logger.info("Connection established!");
                Logger.debug("Users saved before this new connection:");
                for (String key : usersPassword.keySet()) {
                    System.out.println(key + " " + usersPassword.get(key));
                }
                synchronized (syn) {
                    ClientHandler t = new ClientHandler(threadCount, s);
                }
                threadCount++;
            }catch (IOException e){
                Logger.warning("Accept failure.");
            }
        }

        //MainServer ms = new MainServer();
        //ms.start();
    }
}
