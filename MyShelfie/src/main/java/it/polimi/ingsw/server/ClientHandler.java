package it.polimi.ingsw.server;

import it.polimi.ingsw.LobbiesHandler;
import it.polimi.ingsw.Lobby;
import it.polimi.ingsw.User;
import it.polimi.ingsw.UsersHandler;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class ClientHandler extends Thread {
    public static final UsersHandler users = new UsersHandler();
    public static final LobbiesHandler lobbies = new LobbiesHandler();//TODO in clientHandler o MainServer?
    private final int id;
    @Deprecated
    private final Object syn = new Object();
    private String username;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    @Deprecated
    private Scanner sc;
    @Deprecated
    private File accounts;
    @Deprecated
    private HashMap<String, String> usersAddress;


    /**
     * ClientHandler constructor, handles the connection with the client until a new game is created or the client decides to join a game.
     *
     * @param id       thread id, only visible in the server.
     * @param listener client socket.
     */
    public ClientHandler(int id, Socket listener) throws IOException {
        this.id = id;
        this.outputStream = new ObjectOutputStream(listener.getOutputStream());
        this.inputStream = new ObjectInputStream(listener.getInputStream());
        this.start();
        Logger.info("The thread " + id + " is now connected with the player ip " + listener.getRemoteSocketAddress().toString() + "!");
    }

    @Override
    public void run() {
        Message response;
        Lobby lobby;
        try {
            //Ask the client for username and password.
            Message message = new Message(ResponseType.LOGIN_REQUEST); //TODO serve?
            write(message);

            //start listening for requests from client
            while (true) {
                message = read();
                //TODO if user == message.author;
                switch (message.getType()) {
                    case STRING:
                    case CREATE:
                        int lobbyId = lobbies.createLobby(username);
                        users.get(username).setLobbyId(lobbyId);
                        Lobby newLobby = lobbies.get(lobbyId);
                        response = new LobbyJoined(newLobby.id, newLobby.getUsers());
                        Logger.debug("Lobby " + newLobby.id + " created");
                        write(response);
                        //users.sendAll(new LobbyList(lobbies.lobbiesCapacity()));
                        break;
                    case JOIN:
                        response = new LobbyList(lobbies.lobbiesCapacity());
                        write(response); //TODO manda e aggiorna finchè non si unisce a una partita
                        break;
                    case START:
                        if (users.get(username).getLobbyId() != -1) {
                            lobbies.get(users.get(username).getLobbyId()).startGame();
                        } else {
                            Logger.warning("Game can't be started because the user is not in a Lobby");
                        }
                        break;
                    case CURSOR:
                    case TILES:
                    case JOIN_LOBBY:
                        boolean added = false;
                        lobby = lobbies.get(message.lobbyId);

                        if (lobbies.contains(message.lobbyId)) {
                            added = lobby.addUser(message.getAuthor());
                        }
                        if (!lobbies.contains(message.lobbyId) || !added) {
                            response = new Message(ResponseType.JOIN_FAILURE); //TODO JOIN_OUTCOME
                        } else {
                            response = new LobbyJoined(lobby.id, lobby.getUsers());
                        }

                        write(response);
                        //users.sendAll(new LobbyList(lobbies.lobbiesCapacity())); //TODO mandalo solo a quelli che stanno scegliendo lobby

                        break;
                    case NONE:
                        break;
                    case LOGIN_RESPONSE:
                        LoginResponse line = (LoginResponse) message;
                        Logger.debug("Username chosen: " + line.getAuthor() + line.getPassword());
                        if (!users.contains(line.getAuthor())) { //TODO != null constrolla anche che non sia già connesso
                            Logger.debug("Adding username");
                            users.add(new User(line.getAuthor(), line.getPassword(), this));
                            username = line.getAuthor();
                            response = new Message(ResponseType.LOGIN_SUCCESS);
                        } else if (users.contains(line.getAuthor()) && users.get(line.getAuthor()).checkPassword(line.getPassword())) {
                            username = line.getAuthor();
                            response = new Message(ResponseType.LOGIN_SUCCESS);
                        } else {
                            response = new Message(ResponseType.LOGIN_FAILURE);
                        }

                        write(response);
                        break;
                }

            }
        }catch (IOException e){
            Logger.error("An error occurred on thread " + id + " while waiting for connection or with write method.");
        }catch (ClassNotFoundException i){
            Logger.error("An error occurred on thread " + id + " while reading the received object.");
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
        obj.setAuthor(String.valueOf(id));
        outputStream.writeObject(obj);
    }
}
