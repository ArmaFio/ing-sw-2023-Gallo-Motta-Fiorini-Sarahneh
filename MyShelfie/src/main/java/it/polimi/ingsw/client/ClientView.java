package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.LobbiesList;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StateUpdate;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.server.model.TileType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Armando Fiorini
 */
public class ClientView extends Thread {
    public static final char ESC_CHAR = '*';
    static final int SHELF_COLS = 5;
    static final int SHELF_ROWS = 6;
    static final int HEIGHT_WINDOW = 25;
    static final int WIDTH_WINDOW = 125;
    private final Scanner clientInput;
    private final NetworkHandler client;
    public String username; //TODO forse private
    public LobbiesList.LobbyData[] lobbiesData;//TODO private
    private InputHandler inputHandler;
    private GameState state;
    private GamePhase phase;
    private HashMap<Integer, HashMap<String, Integer>> commonCards;
    private TileType[][] personalgoal;
    private Player p;
    private Player[] otherPlayers;
    private String turnHandler;
    private boolean running;
    private String[] lobbyUsers;
    private static String currentPlayer;
    private Tile[][] board;
    private HashMap<String, Tile[][]> shelves; //TODO forse basta tileType
    private Tile[][] availableTiles;
    private int[] availableColumns;


    public ClientView(NetworkHandler client) {
        this.client = client;
        state = GameState.LOGIN;
        clientInput = new Scanner(System.in);
        lobbiesData = new LobbiesList.LobbyData[0];
        lobbyUsers = new String[0];
        board = new Tile[0][0];
        shelves = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            shelves.put("none", new Tile[0][0]);
        }
        start();
    }

    public static void clearScreen() {
        /*
        final String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            try {
                Runtime.getRuntime().exec("cls");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
         */

        for (int i = 0; i < 10; ++i) System.out.println();
    }

    //Windows
    public static String shelfWindow(Tile[][] shelf) {
        StringBuilder window;

        window = new StringBuilder("*╭────┬────┬────┬────┬────╮\n");
        for (int i = 0; i < shelf.length; i++) {
            window.append("*│");
            for (int j = 0; j < shelf[i].length; j++) {
                if (shelf[i][j].getType() == TileType.NONE) {
                    window.append("    │");
                } else {
                    window.append(" ").append(paintTile(shelf[i][j])).append(" │");
                }
            }
            if (shelf.length - 1 != i) {
                window.append("\n").append("*├────┼────┼────┼────┼────┤\n");
            }
        }
        window.append("\n").append("*╰────┴────┴────┴────┴────╯\n\n");

        return window.toString();
    }

    //Usable to paint the personal goal card
    public static String shelfWindow(TileType[][] shelf) {
        StringBuilder window;

        window = new StringBuilder("*╭────┬────┬────┬────┬────╮\n");
        for (int i = 0; i < shelf.length; i++) {
            window.append("*│");
            for (int j = 0; j < shelf[i].length; j++) {
                if (shelf[i][j] == TileType.EMPTY) {
                    window.append("    │");
                } else {
                    window.append(" ").append(paintTile(shelf[i][j])).append(" │");
                }
            }
            if (shelf.length - 1 != i) {
                window.append("\n").append("*├────┼────┼────┼────┼────┤\n");
            }
        }
        window.append("\n").append("*╰────┴────┴────┴────┴────╯\n\n");

        return window.toString();
    }


    /**
     * Gives the string on terminal the game window with the board (to do) or a shelf.
     * Parameters containing additional info to be added.
     *
     * @param board The shelf or board to paint.
     */
    static String paintWindow(Tile[][] board) {
        String boardStr = ClientView.shelfWindow(board);
        StringBuilder window = new StringBuilder(boardStr);

        int boardWidth = boardStr.split("\n")[0].length();
        int boardHeight = boardStr.split("\n").length;

        String info = "Turno di " + currentPlayer + "\n";
        window.insert(0, info);
        info = "Altre info\n";
        window.insert(0, info);

        for (int i = 0; i < window.length(); i++) {
            if (window.charAt(i) == '*') {
                window.replace(i, i + 1, Paint.Space((WIDTH_WINDOW - boardWidth) / 2));
            }
        }

        String buttonsBar = "│ [/v]Change view │ [/c]Chat │ [/s]Settings\n";

        int j = 0;
        StringBuilder upperBar = new StringBuilder("╭\n");

        for (int i = 1; i < buttonsBar.length() - 1; i++) {
            if (buttonsBar.charAt(i + j) == '│') {
                upperBar.insert(i, '┬');
            } else {
                upperBar.insert(i, '─');
            }
        }

        buttonsBar = Paint.Space(WIDTH_WINDOW - buttonsBar.length()) + upperBar
                + Paint.Space(WIDTH_WINDOW - buttonsBar.length()) + buttonsBar;

        window.append(buttonsBar);

        return window.toString();
    }

    public int[] getAvailableColumns() {
        return availableColumns;
    }

    public GamePhase getPhase() {
        return phase;
    }

    public Tile[][] getAvailableTiles() {
        return availableTiles;
    }

    private static String paintTile(Tile tile) {
        //TODO valuta se è meglio hashmap che può essere salvata altrove insieme ad altro
        //String str = tile.type.toString().substring(0, 2);
        String str = "  ";

        switch (tile.type) {
            case CAT -> {
                return Paint.GreenBg(str);
            }
            case BOOK -> {
                return Paint.WhiteBg(str);
            }
            case GAME -> {
                return Paint.YellowBg(str);
            }
            case FRAME -> {
                return Paint.BlueBg(str);
            }
            case TROPHY -> {
                return Paint.CyanBg(str);
            }
            case PLANT -> {
                return Paint.MagentaBg(str);
            }
            default -> {
                return str;
            }
        }
    }

    private static String paintTile(TileType type) {
        //TODO valuta se è meglio hashmap che può essere salvata altrove insieme ad altro
        //String str = tile.type.toString().substring(0, 2);
        String str = "  ";

        switch (type) {
            case CAT -> {
                return Paint.GreenBg(str);
            }
            case BOOK -> {
                return Paint.WhiteBg(str);
            }
            case GAME -> {
                return Paint.YellowBg(str);
            }
            case FRAME -> {
                return Paint.BlueBg(str);
            }
            case TROPHY -> {
                return Paint.CyanBg(str);
            }
            case PLANT -> {
                return Paint.MagentaBg(str);
            }
            default -> {
                return str;
            }
        }
    }

    @Deprecated
    public void setState(GameState state) {
        this.state = state;
    }

    private static String addFrame() {
        StringBuilder window = new StringBuilder();

        window.append('╭').append("─".repeat(WIDTH_WINDOW - 2)).append("╮\n");

        for (int i = 1; i < HEIGHT_WINDOW - 1; i++) {
            window.append('│').append(Paint.Space(WIDTH_WINDOW - 2)).append("│\n");
        }

        window.append('╰').append("─".repeat(WIDTH_WINDOW - 2)).append("╯\n");

        return window.toString();
    }

    /**
     * Main View's Thread, it starts when the game is running: if it's client's turn it waits for it to be completed
     * else it launches turn and interrupts it when the turnHandler has ended his turn
     */
    @Override
    public void run() {
        synchronized (this) {
            System.out.println("Game Started");
            running = true;
            boolean first = true;

            while (running) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Logger.debug("view svegliata");
                switch (state) {
                    case CREATE_JOIN -> {
                        clearScreen();
                        System.out.println("Choose an option:\n[0] Create Lobby\n[1] Join Lobby");
                        if (first) {
                            inputHandler = new InputHandler(this);
                            first = false;
                        }

                    }
                    case LOBBY_CHOICE -> {
                        clearScreen();
                        askLobby(this.lobbiesData);
                    }
                    case INSIDE_LOBBY -> {
                        clearScreen();
                        System.out.println("joined succeed");
                        System.out.println("Users in lobby:");
                        for (String str : lobbyUsers) {
                            System.out.println(str);
                        }

                        if (lobbyUsers.length >= 1) {
                            System.out.println("When you are ready type /start to begin the game");
                        }
                    }
                    case IN_GAME -> {
                        System.out.println("in game state");
                        switch (phase) {
                            case WAIT -> {
                                clearScreen();
                                System.out.println(paintWindow(board));
                                if (shelves.get(username) == null) {
                                    System.out.println(paintWindow(shelves.get("none")));
                                } else {
                                    System.out.println(paintWindow(shelves.get(username)));
                                }
                                turn();
                            }
                            case TILES_REQUEST -> {
                                //System.out.println(paintWindow(board));
                                AskTiles();
                            }
                            case COLUMN_REQUEST -> {
                                //System.out.println(paintWindow(shelves.get(username)));
                                AskColumns();
                            }
                        }
                    }

                }
            }
            //TODO gestire la chiusura della partita e il calcolo del vincitore (lo calcola la view o glielo passa il server?)
        }

    }

    public void declare_winner(String winner) {
        System.out.println(p.getUsername() + ": " + p.getPoints() + " pts");
        for (Player pl : otherPlayers)
            System.out.println(pl.getUsername() + ": " + pl.getPoints() + " pts");
        System.out.println("The winner is: " + winner);
    }

    public int askLobbyDim() {
        int lobbyDim = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("Select the number of players your lobby should have (2-4)");
        while (true) {
            try {
                lobbyDim = sc.nextInt();
                if (lobbyDim >= 2 && lobbyDim <= 4) {
                    break;
                } else {
                    System.out.println("Please select a valid number!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please select a valid number!");
                sc.nextLine();
            }
        }
        return lobbyDim;
    }

    public void onLobbyListMessage(LobbiesList msg) {
        this.lobbiesData = msg.lobbiesData;
    }

    public GameState getGameState() {
        return state;
    }

    /**
     * Updates the game state
     *
     * @param newState new game state
     */
    public synchronized void updateState(GameState newState) {
        this.state = newState;
        Message msg = new StateUpdate(this.state);
        try {
            client.write(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.notifyAll();
    }

    public synchronized void updateState() {
        this.notifyAll();
    }

    public synchronized void updatePhase(GamePhase newPhase) {
        this.phase = newPhase;
    }


    public void askLobby(LobbiesList.LobbyData[] lobbiesData) {
        if (lobbiesData.length == 0) {
            System.out.println("Currently there are no lobbies available\nPlease type /back to go back to the menu or wait for new lobbies!");
            return;
        }
        Logger.info("Choose a Lobby:");
        int cont = 0;
        for (LobbiesList.LobbyData l : lobbiesData) {
            if (l == null) {
                break;
            } else {
                Logger.info("[" + cont + "] " + l.admin + "'s lobby | " + l.capacity + "/" + l.lobbyDim);
                cont++;
            }
        }
    }

    /**
     * Represents the interface's behaviour during opponents' turn, the client can look at "whatever he wants" till the main thread
     * interrupts him (when the turn has ended and the game has to be updated
     */

    public void turn() {
        System.out.println("Menu: \n1)Show Common Objective\n2)Show Personal Objective\n3)Your Shelf\n4)Other Shelves\n");
    }

    public void welcome() {
        System.out.println("Welcome to MyShelfie!\nPlease wait while we connect you to the server!");
    }

    public void connectionEstablished() {
        System.out.println("Connection Established!");
    }

    /**
     * @return login client's credentials
     */
    public String[] loginRequest() {
        String[] credentials = new String[2];
        System.out.println("Enter your username:");
        credentials[0] = clientInput.nextLine().trim();
        System.out.println("Enter the password");
        credentials[1] = clientInput.nextLine().trim();
        return credentials;
    }

    /**
     * @param name refused client's username
     * @return new credentials to be used for new login attempt
     */
    public String[] loginFailed(String name) {
        String[] credentials;
        System.out.println(name + " incorrect/not available\nRetry");
        credentials = loginRequest();
        return credentials;
    }

    /**
     * Informs the client that the lobby has been joined and shows the other players being in
     *
     * @param lobbyUsers array containing lobby members' usernames
     */
    public void onLobbyDataMessage(String[] lobbyUsers) {
        this.lobbyUsers = lobbyUsers;
    }

    public void cantConnect() {
        System.out.println("Cannot connect to the server, keep trying...");
    }

    public void joinFailed() {
        System.out.println("Join failed! :( ");
    }

    public void write(Message message) throws IOException {
        client.write(message);
    }

    public void connectionLost() {
        System.out.println("Connection to the server lost, trying to reconnect...");
    }

    /**
     * Sets the current player that is playing the turn received by the server.
     *
     * @param currentPlayer The current player that is playing the turn.
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Sets the board received by the server.
     *
     * @param board The board received.
     */
    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    /**
     * Sets the shelf for each player received by the server.
     *
     * @param shelves A {@code Map} containing the shelves for each player, with the player's username as the key.
     */
    public void setShelves(HashMap<String, Tile[][]> shelves) {
        this.shelves = shelves;
    }

    /**
     * Sets the common goals status received by the server.
     *
     * @param commonGoals The columns received.
     */
    public void setCommonGoals(HashMap<Integer, HashMap<String, Integer>> commonGoals) {
        this.commonCards = commonGoals;
    }

    /**
     * Sets the available {@code Tile}s for the client, received by the server.
     *
     * @param availableTiles The {@code Tile}s received.
     */
    public void setAvailableTiles(Tile[][] availableTiles) {
        this.availableTiles = availableTiles;
    }

    /**
     * Sets the available columns for the client, received by the server.
     *
     * @param availableColumns The columns received.
     */
    public void setAvailableColumns(int[] availableColumns) {
        this.availableColumns = availableColumns;
    }

    public void AskColumns() {
        System.out.println("Digit the number corresponding to the column you want to insert the tiles in\nAvailable columns: ");
        for (int i : availableColumns)
            System.out.print(i + " ");
    }

    public void AskTiles() {
        int i = 1;
        System.out.println("Digit the number corresponding to the tiles you want to take");
        for (Tile[] comb : availableTiles) {
            System.out.print(i + " ");
            for (Tile t : comb) {
                System.out.print(t.toString() + ", ");
            }
            System.out.println(" ");
            i++;
        }
    }

    public HashMap<String, Tile[][]> getShelves() {
        return shelves;
    }

    public String getUsername() {
        return username;
    }

    public TileType[][] getPersonalgoal() {
        return personalgoal;
    }

    public void setPersonalgoal(TileType[][] personalgoal) {
        this.personalgoal = personalgoal;
    }
}
