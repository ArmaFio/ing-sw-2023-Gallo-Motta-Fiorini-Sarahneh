package it.polimi.ingsw.server;

import com.sun.tools.javac.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
public class MainServer {

    public static void main(String[] args) throws IOException, InterruptedException{
        int threadCount = 0;
        Object syn = new Object();
        ArrayList<String> users = new ArrayList<>();
        HashMap<String, String> usersAddress= new HashMap<>(); //contains the ip associated to the username(the key is the ip)
        Socket s = null;
        ServerSocket ss = null;
        System.out.println("Main server listening...");
        try{
            ss = new ServerSocket(59090);
        }catch (IOException e){
            System.out.println("Failed in creating a socket.");
        }
        while(true){
            try{
                s = ss.accept();
                System.out.println("Connection established!");
                System.out.println("Users saved before this new connection:");
                for(String key : usersAddress.keySet()){
                    System.out.println(key + " " + usersAddress.get(key));
                }
                synchronized (syn){
                    usersAddress.put(s.getRemoteSocketAddress().toString(), null);
                    ClientHandler t = new ClientHandler(threadCount, s, users, usersAddress);
                    t.start();
                }
                threadCount++;
            }catch (IOException e){
                System.out.println("Accept failure.");
            }
        }

        //MainServer ms = new MainServer();
        //ms.start();
    }
}
