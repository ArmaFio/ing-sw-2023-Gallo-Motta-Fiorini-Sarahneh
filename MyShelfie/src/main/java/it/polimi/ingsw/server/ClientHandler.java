package it.polimi.ingsw.server;

import it.polimi.ingsw.LobbiesHandler;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.UsersHandler;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.utils.LoadSave;
import it.polimi.ingsw.utils.Logger;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private final int num;
    private final Socket socket;
    private Scanner sc;
    private String player;
    public static final LobbiesHandler lobbies = new LobbiesHandler();//TODO in clientHandler o MainServer?
    private final Object syn = new Object();
    private HashMap<String, String> usersPassword;
    private File accounts;
    public static final UsersHandler users = new UsersHandler();
    private String user;
    private HashMap<String, String> usersAddress;
    private int lobbyId;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

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
        lobbyId = -1;
    }

    @Override
    public void run() {
        Message response;
        Lobby lobby;
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
            outputStream = new ObjectOutputStream(output);
            PrintWriter writer = new PrintWriter(output, true);
            inputStream = new ObjectInputStream(input);

            //Ask the client for username and password.
            Message message = new LoginRequest("Server");
            write(message);

            //start listening for requests from client
            while (true) {
                message = read();
                //TODO if user == message.author;
                switch (message.getType()) {
                    case STRING:
                    case CREATE:
                        lobbyId = lobbies.createLobby(user, this);
                        Lobby newLobby = lobbies.get(lobbyId);
                        response = new LobbyJoined(user, newLobby.id, newLobby.getUsers());
                        write(response);
                    case JOIN:
                        response = new LobbyList(user, lobbies.lobbiesCapacity());
                        write(response); //TODO manda e aggiorna finchè non si unisce a una partita
                        break;
                    case START:
                        if (lobbyId != -1) {
                            lobbies.get(lobbyId).startGame();
                        } else {
                            Logger.warning("Game can't be started because the user is not in a Lobby");
                        }
                        break;
                    case CURSOR:
                    case TILES:
                    case JOIN_LOBBY:
                        boolean added = false;
                        response = new Message(message.getAuthor());
                        lobby = lobbies.get(message.lobbyId);

                        if (lobby != null) {
                            lobbyId = lobby.id;
                            added = lobby.addUser(message.getAuthor());
                            response.setType(ResponseType.JOIN_SUCCESS);

                        }
                        if (lobby == null || added) {
                            response.setType(ResponseType.JOIN_FAILURE);
                        }

                        write(response);
                        users.sendAll(new LobbyList(user, lobbies.lobbiesCapacity())); //TODO mandalo solo a quelli che stanno

                        break;
                    case NONE:
                    case LOGIN_RESPONSE:
                        LoginResponse line = (LoginResponse) message;
                        System.out.println("Username chosen: " + line.getAuthor());//only for debug
                        if (usersPassword.get(line.getAuthor()) != null && !usersPassword.get(line.getAuthor()).equals(line.getPassword())) {
                            response = new LoginOutcome("Server", "Failure", "Incorrect password for the username " + line.getAuthor());
                            write(response);
                        }
                        if (usersPassword.containsKey(line.getAuthor()) && usersPassword.get(line.getAuthor()).equals(line.getPassword())) {
                            response = new LoginOutcome("Server", "Success", "Logged in");
                            write(response);
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
                            write(response);
                        }
                        break;
                }

            }
        }catch (IOException e){
            Logger.error("An error occurred on thread " + num + " while waiting for connection or with write method.");
        }catch (ClassNotFoundException i){
            Logger.error("An error occurred on thread " + num + " while reading the received object.");
        }
    }

    /**
     * Reads a serialized object received from the client.
     *
     * @return the object read.
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Message read() throws ClassNotFoundException, IOException {
        return (Message) inputStream.readObject();
    }

    /*
    public Response waitFor(ResponseType type){
        while(newMessage.getType() != type){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return newMessage;
    }
    */

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param obj the object we want to send to the client.
     * @throws IOException
     */
    public void write(Message obj) throws IOException {
        //TODO obj.setAuthor(idClientHandler)
        obj.setAuthor(user); //TODO potrebbe non esistere
        outputStream.writeObject(obj);
    }
}
