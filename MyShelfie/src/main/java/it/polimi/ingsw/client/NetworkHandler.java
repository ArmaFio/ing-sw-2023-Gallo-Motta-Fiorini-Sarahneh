package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.Logger;
import javafx.application.Application;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NetworkHandler {
    public static final String ANSIRed = "\u001B[31m";
    public static final String ANSIReset = "\u001B[0m";
    private final boolean first = true;
    @Deprecated
    private final Scanner sc = new Scanner(System.in);
    public static boolean init = false;
    private boolean running;
    private boolean connected = false; //TODO fai locale
    private boolean firstTime = true; //TODO fai locale
    private String username;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    @Deprecated
    private BufferedReader reader;
    @Deprecated
    private PrintWriter writer;
    private final View view;

    public NetworkHandler(int choice) throws IOException {
        if (choice == 0) {
            view = new ViewCLI(this);
        } else {
            Thread initialize = new Thread() {
                @Override
                public void run() {
                    Application.launch(ViewGUI.class);
                }
            };
            initialize.start();
            while (true) {
                try {
                    synchronized (this) {
                        wait(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (init) {
                    view = ViewGUI.getInstance();
                    view.setClient(this);
                    break;
                }
            }
        }
        //FXMLLoader loader = new FXMLLoader(new File("MyShelfie/src/main/resources/main-view.fxml").toURI().toURL());
        //Parent root = loader.load();
        //LoginController controller = loader.getController();
        //try until connection succeeds.
        connect();
        //start listening for server instructions

        start();
    }

    private void start(){
        running = true;
        while (running) {
            try (Message message = read()) {
                switch (view.getGameState()) {
                    case LOGIN -> {
                        switch (message.getType()) {
                            case LOGIN_REQUEST -> view.updateState(GameState.LOGIN);
                            case LOGIN_FAILURE -> {
                                view.setUsername("");
                                view.setPassword("");
                                view.updateState();
                            }
                            case LOGIN_SUCCESS -> {
                                Logger.info(message.getAuthor() + " logged");
                                setUsername(message.getAuthor());
                                view.updateState(GameState.CREATE_JOIN);
                            }
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");
                        }
                    }
                    case CREATE_JOIN -> {
                        switch (message.getType()) {
                            case JOIN_SUCCEED -> view.updateState(GameState.INSIDE_LOBBY);
                            case JOIN_FAILURE -> {
                                Logger.error("Join failed! :( ");
                                view.updateState(GameState.CREATE_JOIN);
                            }
                            case LOBBIES_LIST -> {
                                LobbiesList msg = (LobbiesList) message;
                                if (!msg.update) {
                                    view.onLobbyListMessage(msg);
                                    view.updateState(GameState.LOBBY_CHOICE);
                                } else {
                                    view.onLobbyListMessage(msg);
                                }
                            }
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");
                        }
                    }
                    case LOBBY_CHOICE -> {
                        switch (message.getType()) {
                            case JOIN_SUCCEED -> view.updateState(GameState.INSIDE_LOBBY);
                            case JOIN_FAILURE -> {
                                System.out.println("Join failed! :( ");
                                view.updateState(GameState.CREATE_JOIN);
                            }
                            case LOBBIES_LIST -> {
                                view.onLobbyListMessage((LobbiesList) message);
                                view.updateState();
                            }
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");
                        }
                    }
                    case INSIDE_LOBBY -> {
                        switch (message.getType()) {
                            case LOBBY_DATA -> {
                                String[] lobbyUsers = ((LobbyData) message).getLobbyUsers();
                                view.onLobbyDataMessage(lobbyUsers);
                                view.updateState();
                            }
                            case START -> {
                                view.setPersonalGoal(((StartMessage) message).getPersonalGoal());
                                view.setCommonGoals(((StartMessage) message).getCommonsGoals());
                                view.updatePhase(GamePhase.WAIT);
                                view.updateState(GameState.IN_GAME);
                            }
                            case STRING -> {
                                StringRequest notify = (StringRequest) message;
                                view.onStringMessage(notify.message());
                            }
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");

                        }
                    }
                    case IN_GAME -> {
                        /*
                        if (first) {
                            view.updateState(GameState.IN_GAME);
                            first = false;
                        }
                         */
                        switch (message.getType()) {
                            case GAME_UPD -> {
                                GameUpdate update = (GameUpdate) message;

                                view.onGameUpdate(update);

                                view.updatePhase(GamePhase.WAIT);
                                view.updateState();
                            }
                            case TILES_REQUEST -> {
                                TilesRequest request = (TilesRequest) message;

                                Tile[][] availableTiles = request.getAvailableTiles();
                                view.onAvailableTiles(availableTiles);
                                view.updatePhase(GamePhase.TILES_REQUEST);
                                view.updateState();
                            }
                            case COLUMN_REQUEST -> {
                                ColumnRequest request = (ColumnRequest) message;

                                int[] availableColumns = request.getAvailableColumns();
                                view.onAvailableColumns(availableColumns);
                                view.updatePhase(GamePhase.COLUMN_REQUEST);
                                view.updateState();
                            }
                            case STRING -> {
                                StringRequest notify = (StringRequest) message;
                                System.out.println(notify.message());
                            }
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");
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
                System.out.println("Connection to the server lost, trying to reconnect..."); //TODO non in tutti i casi si è disconnesso
                connect();
            }
        }
    }

    /**
     * Creates a connection between client and server, keeps trying until success.
     */
    void connect() {
        connected = false;
        firstTime = true;
        while (!connected) {
            try {
                Socket socket = new Socket("127.0.0.1", 59090);
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                outputStream = new ObjectOutputStream(output);
                inputStream = new ObjectInputStream(input);
                firstTime = true;
                connected = true;
                System.out.println("Connection Estabilished!");
            } catch (IOException e) {
                if (firstTime) {
                    System.out.println("Cannot connect to the server, keep trying...");
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
        obj.setAuthor(username);
        outputStream.writeObject(obj);
    }

    void setUsername(String username) {
        this.username = username;
        view.setUsername(username);
    }

    void disconnect(){
        view.updateState(GameState.CLOSE);
        view.disconnect();
    }

    public void reconnect() { //TODO controlla se non fa casini di sync con read()0
        view.updateState(GameState.CLOSE);
        view.updateState(GameState.LOGIN);

        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        connect();
    }
}
