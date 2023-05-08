package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.server.model.TileType;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class NetworkHandler {
    public static final String ANSIRed = "\u001B[31m";
    public static final String ANSIReset = "\u001B[0m";
    private final boolean running = true;
    private boolean first = true;
    @Deprecated
    private final Scanner sc = new Scanner(System.in);
    private final ClientView view;
    private boolean connected = false; //TODO fai locale
    private boolean firstTime = true; //TODO fai locale
    private String username;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    @Deprecated
    private BufferedReader reader;
    @Deprecated
    private PrintWriter writer;

    public NetworkHandler() {
        String[] credentials = new String[2];
        LoginResponse login;
        Message response;
        view = new ClientView(this);
        System.out.println(ANSIRed + "  __  ____     __   _____ _    _ ______ _      ______ _____ ______ \n" +
                " |  \\/  \\ \\   / /  / ____| |  | |  ____| |    |  ____|_   _|  ____|\n" +
                " | \\  / |\\ \\_/ /  | (___ | |__| | |__  | |    | |__    | | | |__   \n" +
                " | |\\/| | \\   /    \\___ \\|  __  |  __| | |    |  __|   | | |  __|  \n" +
                " | |  | |  | |     ____) | |  | | |____| |____| |     _| |_| |____ \n" +
                " |_|  |_|  |_|    |_____/|_|  |_|______|______|_|    |_____|______|\n" +
                "                                                                   \n" +
                "                                                                   " + ANSIReset);
        System.out.println("Welcome to MyShelfie!\nPlease wait while we connect you to the server!");
        //try until connection succeeds.
        connect();
        //start listening for server instructions
        while (running) {
            try (Message message = read()) {
                switch (view.getGameState()) {
                    case LOGIN -> {
                        switch (message.getType()) {
                            case LOGIN_REQUEST -> {
                                setUsername(message.getAuthor());
                                credentials = view.loginRequest();
                                login = new LoginResponse(credentials[0], credentials[1]);
                                username = credentials[0]; //TODO qua o dopo login Success?
                                view.username = credentials[0];
                                try {
                                    write(login);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                //view.updateState(GameState.LOGIN);
                            }
                            case LOGIN_FAILURE -> {
                                credentials = view.loginFailed(credentials[0]);
                                login = new LoginResponse(credentials[0], credentials[1]);
                                username = credentials[0];
                                write(login);
                            }
                            case LOGIN_SUCCESS -> {
                                Logger.info(credentials[0] + " loggato");

                                view.updateState(GameState.CREATE_JOIN);
                            }
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");
                        }
                    }
                    case CREATE_JOIN -> {
                        switch (message.getType()) {
                            case JOIN_SUCCEED -> view.updateState(GameState.INSIDE_LOBBY);
                            case JOIN_FAILURE -> {
                                System.out.println("Join failed! :( ");
                                view.updateState(GameState.CREATE_JOIN);
                            }
                            case LOBBIES_LIST -> {
                                LobbiesList mess = (LobbiesList) message;
                                if (!mess.update) {
                                    view.onLobbyListMessage((LobbiesList) message);
                                    view.updateState(GameState.LOBBY_CHOICE);
                                } else {
                                    view.onLobbyListMessage((LobbiesList) message);
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
                                System.out.println("The game is about to start!");
                                view.setPersonalgoal(((StartMessage) message).getPgc());
                                view.setState(GameState.IN_GAME);
                                view.updatePhase(GamePhase.WAIT);//TODO inserisci le personal goal card (solo di questo user)
                            }
                            case STRING -> {
                                StringRequest notify = (StringRequest) message;
                                System.out.println(notify.message());
                            }
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");

                        }
                    }
                    case IN_GAME -> {
                        if (first) {
                            view.updateState(GameState.IN_GAME);
                            first = false;
                        }
                        switch (message.getType()) {
                            case GAME_UPD -> {
                                GameUpdate update = (GameUpdate) message;

                                view.setCurrentPlayer(update.playerTurn);
                                view.setBoard(update.getBoard());
                                view.setShelves(update.getShelves());
                                view.setCommonGoals(update.getCommonGoals());
                                Logger.debug("Arrivo in update game");
                                view.updatePhase(GamePhase.WAIT);
                                view.updateState();
                            }
                            case TILES_REQUEST -> {
                                TilesRequest request = (TilesRequest) message;

                                Tile[][] availableTiles = request.getAvailableTiles();
                                view.setAvailableTiles(availableTiles);
                                view.updatePhase(GamePhase.TILES_REQUEST);
                                view.updateState();
                            }
                            case COLUMN_REQUEST -> {
                                ColumnRequest request = (ColumnRequest) message;

                                int[] availableColumns = request.getAvailableColumns();
                                view.setAvailableColumns(availableColumns);
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

    public void setUsername(String username) {
        this.username = username;
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
