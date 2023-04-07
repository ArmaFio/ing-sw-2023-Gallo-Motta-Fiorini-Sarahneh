package it.polimi.ingsw.server;

import com.sun.tools.javac.Main;
import it.polimi.ingsw.Response;
import it.polimi.ingsw.User;
import it.polimi.ingsw.response.LoginRequest;
import it.polimi.ingsw.response.StringRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler extends Thread{
    private final int num;
    private ServerSocket listener;
    private final Socket socket;
    private Scanner sc;
    private String player;
    private ArrayList<String> users;
    private final Object syn = new Object();
    private HashMap<String, String> usersAddress;

    /**
     * ClientHandler constructor, handles the connection with the client until a new game is created or the client decides to join a game.
     * @param num thread id, only visible in the server.
     * @param s client socket.
     * @param users list of usernames already registered.
     */
    public ClientHandler(int num, Socket s, ArrayList<String> users, HashMap<String,String> usersAddress){
        this.num = num;
        socket = s;
        this.users = users;
        this.usersAddress = usersAddress;
    }

    @Override
    public void run(){
        try {
            System.out.println("The thread " + num + " is now connected with the player " + socket.getRemoteSocketAddress().toString() + "!");
            System.out.println("In lista:");//only for debug
            for(String s : users){
                System.out.println(s);
            }
            //initialize variables
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            PrintWriter writer = new PrintWriter(output, true);
            ObjectInputStream objectInputStream = new ObjectInputStream(input);

            //Ask the client for the username.
            Response response = new LoginRequest();
            write(objectOutputStream, response);
            String line = reader.readLine();
            System.out.println("Username chosen: " + line);//only for debug
            usersAddress.replace(socket.getRemoteSocketAddress().toString(), null, line);// binds the username to the ip.


            while(users.contains(line)){
                writer.println("Failure");
                line = reader.readLine();
            }
            System.out.println("Aggiungo username");//only for debug
            writer.println("Success");
            users.add(line);

            //start listening for requests from client
            while(true){
                response = read(objectInputStream);
                switch (response.type){
                    case STRING:
                    case START:
                    case CURSOR:
                    case TILES:
                    case JOIN:
                    case NONE:
                }

            }
        }catch (IOException e){
            System.out.println("An error occurred on thread " + num + " while waiting for connection or with write method.");
        }catch (ClassNotFoundException i){
            System.out.println("An error occurred on thread " + num + " while reading the received object.");
        }
    }

    /**
     * Reads a serialized object received from the client.
     * @param objectInputStream the InputStream for serialized objects.
     * @return the object read.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Response read(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        return (Response) objectInputStream.readObject();
    }

    /**
     * Writes a serialized object and sends it to the client.
     * @param objectOutputStream the OutputStream for the serialized object.
     * @param obj the object we want to send to the client.
     * @throws IOException
     */
    public void write(ObjectOutputStream objectOutputStream, Response obj) throws IOException{
        objectOutputStream.writeObject(obj);
    }
}
