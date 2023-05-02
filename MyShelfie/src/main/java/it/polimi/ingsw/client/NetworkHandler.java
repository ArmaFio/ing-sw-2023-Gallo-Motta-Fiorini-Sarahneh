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
        String[] credentials = new String[2];
        LoginResponse login;
        Message response;
        view = new ClientView(this);
        view.welcome();
        //try until connection succeeds.
        connect();
        //start listening for server instructions
        while (running) {
            try (Message message = read()) {
                switch (view.getGameState()) {
                    case LOGIN -> {
                        switch (message.getType()) {
                            case LOGIN_REQUEST -> {
                                credentials = view.loginRequest();
                                login = new LoginResponse(credentials[0], credentials[1]);
                                user = credentials[0]; //TODO qua o dopo login Success?
                                try {
                                    this.write(login);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                view.updateState(GameState.LOGIN);
                            }
                            case LOGIN_FAILURE -> {
                                credentials = view.loginFailed(credentials[0]);
                                login = new LoginResponse(credentials[0], credentials[1]);
                                user = credentials[0];
                                write(login);
                            }
                            case LOGIN_SUCCESS -> {
                                Logger.info(credentials[0] + " loggato");

                                view.updateState(GameState.CREATE_JOIN);
                            }
                        }
                    }
                    /*
                    case CREATE_JOIN -> {
                        switch (message.getType()) {
                            case LOBBY_LIST ->{
                                view.onLobbyListMessage((LobbyList) message);
                                view.updateState(GameState.LOBBY_CHOICE); //TODO deve farlo inputHandler
                            }
                        }
                    }
                    */
                    case LOBBY_CHOICE -> {
                        switch (message.getType()) {
                            case JOIN_SUCCESS -> {
                                String[] lobbyUsers = ((JoinSuccess) message).getLobbyUsers();
                                view.joinSuccess(lobbyUsers);
                                view.updateState(GameState.INSIDE_LOBBY);
                            }
                            case JOIN_FAILURE -> {
                                view.joinFailed();
                                view.updateState(GameState.CREATE_JOIN);
                            }
                            case LOBBY_LIST ->{
                                view.onLobbyListMessage((LobbyList) message);
                                view.updateState(GameState.LOBBY_CHOICE);
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
                    default -> Logger.warning("messaggio ignorato");
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
                view.connectionLost(); //TODO non in tutti i casi si è disconnesso
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
                view.connectionEstabilished();
            } catch (IOException e) {
                if (firstTime) {
                    view.cantConnect();
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

    public void setUser(String username) {
        this.user = username;
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
