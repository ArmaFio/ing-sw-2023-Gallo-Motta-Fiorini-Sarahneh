package it.polimi.ingsw.server;

import it.polimi.ingsw.Response;
import it.polimi.ingsw.model.utils.LoadSave;
import it.polimi.ingsw.response.LoginOutcome;
import it.polimi.ingsw.response.LoginRequest;
import it.polimi.ingsw.response.LoginResponse;
import it.polimi.ingsw.response.StringRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private final int num;
    private ServerSocket listener;
    private final Socket socket;
    private Scanner sc;
    private String player;
    private final Object syn = new Object();
    private HashMap<String, String> usersPassword;
    private File accounts;

    /**
     * ClientHandler constructor, handles the connection with the client until a new game is created or the client decides to join a game.
     *
     * @param num thread id, only visible in the server.
     * @param s   client socket.
     */
    public ClientHandler(int num, Socket s, HashMap<String, String> usersPassword, File accounts) {
        this.num = num;
        socket = s;
        this.usersPassword = usersPassword;
        this.accounts = accounts;
    }

    @Override
    public void run(){
        try {
            System.out.println("The thread " + num + " is now connected with the player ip " + socket.getRemoteSocketAddress().toString() + "!");
            System.out.println("In lista:");//only for debug
            for (String key : usersPassword.keySet()) {
                System.out.println(key + " " + usersPassword.get(key));
            }
            //initialize variables
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);
            PrintWriter writer = new PrintWriter(output, true);
            ObjectInputStream objectInputStream = new ObjectInputStream(input);

            //Ask the client for username and password.
            Response response = new LoginRequest("Server");
            write(objectOutputStream, response);

            //start listening for requests from client
            while (true) {
                response = read(objectInputStream);
                switch (response.type) {
                    case STRING:
                    case START:
                    case CURSOR:
                    case TILES:
                    case JOIN:
                    case NONE:
                    case LOGIN_RESPONSE:
                        LoginResponse line = (LoginResponse) response;
                        System.out.println("Username chosen: " + line.getAuthor());//only for debug
                        if (usersPassword.get(line.getAuthor()) != null && !usersPassword.get(line.getAuthor()).equals(line.getPassword())) {
                            response = new LoginOutcome("Server", "Failure", "Incorrect password for the username " + line.getAuthor());
                            write(objectOutputStream, response);
                        }
                        if (usersPassword.containsKey(line.getAuthor()) && usersPassword.get(line.getAuthor()).equals(line.getPassword())) {
                            response = new LoginOutcome("Server", "Success", "Logged in");
                            write(objectOutputStream, response);
                        }
                        if (!usersPassword.containsKey(line.getAuthor())) {
                            System.out.println("Adding username");//only for debug
                            usersPassword.put(line.getAuthor(), line.getPassword());// binds the username to the password.
                            try {
                                LoadSave.write(accounts.getPath(), usersPassword);
                            } catch (RuntimeException e) {
                                System.out.println("An error occurred while saving the file!");
                            }
                            response = new LoginOutcome("Server", "Success", "Account created, you are now logged in as " + line.getAuthor());
                            write(objectOutputStream, response);
                        }
                        break;
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
