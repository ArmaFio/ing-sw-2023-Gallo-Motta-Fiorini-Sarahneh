package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.utils.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NetworkHandler extends Thread {
    private boolean connected = false; //TODO fai locale
    private boolean firstTime = true; //TODO fai locale
    private boolean running = true;
    @Deprecated
    private Scanner sc = new Scanner(System.in);
    private String user;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    @Deprecated
    private BufferedReader reader;
    @Deprecated
    private PrintWriter writer;
    private ClientView view;

    public NetworkHandler() {
        start();
    }

    @Override
    public void run() {
        String username, password;
        LoginResponse login;
        Message response;
        System.out.println("Welcome to MyShelfie!\nPlease wait while we connect you to the server!");
        view = new ClientView(this);
        //try until connection succeeds.
        connect();
        //start listening for server instructions
        while (running) {
            try (Message message = read()) {
                switch (view.getGameState()) {
                    case LOGIN -> {
                        switch (message.getType()) {
                            case LOGIN_REQUEST -> {
                                System.out.println("Enter your username:");
                                username = sc.nextLine().trim();
                                System.out.println("Enter the password");
                                password = sc.nextLine().trim();
                                login = new LoginResponse(username, password);
                                user = username;
                                write(login);
                            }
                            case LOGIN_FAILURE -> {
                                Logger.info(user + " non corretto!");
                                System.out.println("Enter your username:"); //TODO askCredential() di view
                                username = sc.nextLine().trim();
                                System.out.println("Enter the password");
                                password = sc.nextLine().trim();
                                login = new LoginResponse(username, password);
                                user = username;
                                write(login);
                            }
                            case LOGIN_SUCCESS -> {
                                Logger.info(user + " connesso");

                                if (view.askJoinOrCreate()) {
                                    response = new Message(ResponseType.CREATE);
                                } else {
                                    response = new Message(ResponseType.JOIN);
                                }

                                write(response);
                                view.updateState(GameState.LOBBY_CHOICE);
                            }
                        }
                    }
                    case LOBBY_CHOICE -> {
                        switch (message.getType()) {
                            case JOIN_SUCCESS -> {
                                String[] lobbyUsers = ((JoinSuccess) message).getLobbyUsers();
                                Logger.info("joined succeed");
                                Logger.info("Users in lobby:");
                                for (String str : lobbyUsers) {
                                    Logger.info(str);
                                }
                                view.updateState(GameState.INSIDE_LOBBY);
                            }
                            case JOIN_FAILURE -> {
                                Logger.warning("joined failed");
                                //TODO manda un'altra richiesta
                            }
                            case LOBBY_LIST -> {
                                view.onLobbyListMessage((LobbyList) message);
                            }
                        }
                    }
                    case INSIDE_LOBBY -> {
                        switch (message.getType()) {
                            case START -> {
                            }
                            case LOBBY_DATA -> {
                            }
                        }
                    }
                    default -> {
                        Logger.warning("messaggio ignorato");
                    }
                }
                /*
                switch (message.getType()) {
                    case NONE:
                        break;


                    case TILES:
                        TilesRequest res = (TilesRequest) message;
                        ArrayList<ArrayList<Tile>> sel = res.getAvailable();
                        int i = view.vPickTiles(sel);
                        res.setChosen(sel.get(0));
                        res.setCol(i);
                        write(res);
                        break;
                    case CURSOR:
                        break;
                    case START:
                        StartRequest play = (StartRequest) message;
                        view.setGame(play.getCurrentGame(), username);
                        break;
                    case STRING:
                        //TODO this will be probably  used for the chat
                        StringRequest line = (StringRequest) message;
                        System.out.println(line.message());
                        break;

                    default:

                }
                 */
            } catch (ClassNotFoundException e) {
                System.out.println("Error: Class not found!");
            } catch (IOException e) {
                Logger.error("Connection to the server lost, trying to reconnect..." + e); //TODO non in tutti i casi si è disconnesso
                connect();
            }
        }
    }

    /**
     * Creates a connection between client and server, keeps trying until success.
     */
    private void connect() {
        connected = false;
        while (!connected) {
            try {
                Socket socket = new Socket("127.0.0.1", 59090);
                InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                outputStream = new ObjectOutputStream(output);
                writer = new PrintWriter(output, true);
                inputStream = new ObjectInputStream(input);
                firstTime = true;
                connected = true;
                Logger.info("Connection established!");
            } catch (IOException e) {
                if (firstTime) {
                    Logger.warning("Cannot connect to the server, keep trying...");
                    firstTime = false;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException i) {
                    Logger.error("InterruptedException occurred!");
                }
            }
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
        Message msg = (Message) inputStream.readObject();
        Logger.info("Message " + msg.getType().toString() + " received");
        return msg;
    }

    /**
     * Writes a serialized object and sends it to the client.
     *
     * @param obj the object we want to send to the client.
     * @throws IOException
     */
    public void write(Message obj) throws IOException {
        obj.setAuthor(user);
        outputStream.writeObject(obj);
    }
}
//public void send(String s) {
//  writer.println(s);
//}

// public String read() throws IOException {
//   return reader.readLine();
//}

//public void close() throws IOException {
//  socket.close();
//}
