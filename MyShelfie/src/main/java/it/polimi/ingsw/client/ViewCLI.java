package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.javafx.Controller;
import it.polimi.ingsw.messages.GameUpdate;
import it.polimi.ingsw.messages.LobbiesList;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StateUpdate;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Armando Fiorini
 */
public class ViewCLI extends Thread implements View {
    public static final char ESC_CHAR = '*';
    static final int SHELF_COLS = 5;
    static final int SHELF_ROWS = 6;
    static final int HEIGHT_WINDOW = 25;
    static final int WIDTH_WINDOW = 125;
    private final NetworkHandler client;
    public String username; //TODO forse private
    public LobbiesList.LobbyData[] lobbiesData;//TODO private
    private final InputHandler inputHandler;
    private GameState state;
    private GamePhase phase;
    private HashMap<Integer, HashMap<String, Integer>> commonCards;
    private TileType[][] personalGoal;
    private String[] players;
    private boolean running;
    private String[] lobbyUsers;
    private String currentPlayer;
    private Tile[][] board;
    private HashMap<String, Tile[][]> shelves; //TODO forse basta tileType
    private Tile[][] availableTiles;
    private int[] availableColumns;
    private final FrameCLI frame;
    private int menuValue;
    private int boardViewed;
    public static final String ANSIRed = "\u001B[31m";
    public static final String ANSIReset = "\u001B[0m";


    public ViewCLI(NetworkHandler client) {
        this.client = client;
        this.state = GameState.LOGIN;
        this.lobbiesData = new LobbiesList.LobbyData[0];
        this.inputHandler = new InputHandler(this);
        this.frame = new FrameCLI(WIDTH_WINDOW, HEIGHT_WINDOW);
        this.lobbyUsers = new String[0];
        this.board = new Tile[0][0];
        this.shelves = new HashMap<>();
        this.menuValue = -1;
        this.boardViewed = 0;
        for (int i = 0; i < 4; i++) {
            shelves.put("none", new Tile[0][0]);
        }
        this.start();
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


    @Deprecated
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Main View's Thread, it starts when the game is running: if it's client's turn it waits for it to be completed
     * else it launches turn and interrupts it when the turnHandler has ended his turn
     */
    @Override
    public void run() {
        //TODO ?
        running = true;
        boolean first = true;
        synchronized (this) {
            while (running) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                switch (state) {
                    case LOGIN -> {
                        System.out.println(mainMenu());
                        System.out.println("Enter your username:");
                    }
                    case CREATE_JOIN -> {
                        frame.clearScreen();
                        System.out.println("Choose an option:\n[0] Create Lobby\n[1] Join Lobby");
                        if (first) {

                            first = false;
                        }

                    }
                    case LOBBY_CHOICE -> {
                        frame.clearScreen();
                        askLobby(this.lobbiesData);
                    }
                    case INSIDE_LOBBY -> {
                        frame.clearScreen();
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
                        switch (phase) {
                            case WAIT -> {
                                frame.paintWindow("Turn of " + currentPlayer, getBoardViewed(), lobbyUsers, menuValue);
                                /*
                                if (shelves.get(username) == null) {
                                    frame.paintWindow("Turn of -", shelves.get("none"), -1);
                                } else {
                                    frame.paintWindow("Turn of -", shelves.get(username), -1);
                                  }
                                 */
                            }
                            case TILES_REQUEST -> {
                                //setBoardViewed(shelves.get() + 1);
                                frame.paintWindow("Your turn | Digit the coordinates corresponding to the tiles you want to take! Format: B3 B4", getBoardViewed(), lobbyUsers, menuValue);
                            }
                            case COLUMN_REQUEST ->
                                    frame.paintWindow("Your turn | Digit the character corresponding to the column you want to insert the tiles in", getBoardViewed(), lobbyUsers, menuValue);
                        }
                    }

                }
            }
            //TODO gestire la chiusura della partita e il calcolo del vincitore (lo calcola la view o glielo passa il server?)
        }
    }

    private String mainMenu() {
        return (ANSIRed + "  __  ____     __   _____ _    _ ______ _      ______ _____ ______ \n" +
                " |  \\/  \\ \\   / /  / ____| |  | |  ____| |    |  ____|_   _|  ____|\n" +
                " | \\  / |\\ \\_/ /  | (___ | |__| | |__  | |    | |__    | | | |__   \n" +
                " | |\\/| | \\   /    \\___ \\|  __  |  __| | |    |  __|   | | |  __|  \n" +
                " | |  | |  | |     ____) | |  | | |____| |____| |     _| |_| |____ \n" +
                " |_|  |_|  |_|    |_____/|_|  |_|______|______|_|    |_____|______|\n" +
                "                                                                   \n" +
                "                                                                   " + ANSIReset);
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
     * Informs the client that the lobby has been joined and shows the other players being in
     *
     * @param lobbyUsers array containing lobby members' usernames
     */
    public void onLobbyDataMessage(String[] lobbyUsers) {
        this.lobbyUsers = lobbyUsers;
    }

    @Override
    public void write(Message message) throws IOException {
        client.write(message);
    }

    /**
     * Sets the current player that is playing the turn, the board, the shelf for each player and the common goals status, received by the server.
     *
     * @param update The message received by the server containing all the updates.
     */
    @Override
    public void onGameUpdate(GameUpdate update) {
        this.currentPlayer = update.playerTurn;
        this.board = update.getBoard();
        this.shelves = update.getShelves();
        this.commonCards = update.getCommonGoals();
        this.lobbyUsers = this.shelves.keySet().toArray(new String[0]);
    }


    /**
     * Sets the available {@code Tile}s for the client, received by the server.
     *
     * @param availableTiles The {@code Tile}s received.
     */
    public void onAvailableTiles(Tile[][] availableTiles) {
        this.availableTiles = availableTiles;
    }

    /**
     * Sets the available columns for the client, received by the server.
     *
     * @param availableColumns The columns received.
     */
    public void onAvailableColumns(int[] availableColumns) {
        this.availableColumns = availableColumns;
    }

    @Deprecated
    public void AskColumns() {
        System.out.println("Digit the number corresponding to the column you want to insert the tiles in\nAvailable columns: ");
        for (int i : availableColumns)
            System.out.print(i + " ");
    }

    @Deprecated
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

    public TileType[][] getPersonalGoal() {
        return personalGoal;
    }

    public void setPersonalGoal(TileType[][] personalGoal) {
        this.personalGoal = personalGoal;
    }

    public int getMenuValue() {
        return this.menuValue;
    }

    public void setMenuValue(int choice) {
        if (choice >= -1 && choice <= 2) {
            this.menuValue = choice;
        }
    }

    private Tile[][] getBoardViewed() {
        int i = 1;
        for (String player : shelves.keySet()) {
            if (this.boardViewed == i) {
                return shelves.get(player);
            }
            i++;
        }

        return this.board;
    }

    public void setBoardViewed(int choice) {
        if (choice >= 0 && choice <= shelves.size()) {
            this.boardViewed = choice;
        }
    }

    public void setMessage(String msg) {
    }

    public Tile getTileFromBoard(int i, int j) {
        return this.board[this.board.length - i][j];
    }
}
