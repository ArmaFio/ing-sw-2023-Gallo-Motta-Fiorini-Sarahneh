package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.LobbiesList;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StateUpdate;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private Player p;
    private Player[] otherPlayers;
    private String turnHandler;
    private boolean running;
    private String[] lobbyUsers;
    private String currentPlayer;
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

        for (int i = 0; i < 50; ++i) System.out.println();
    }

    //Windows
    public static String shelfWindow(Tile[][] shelf) {
        StringBuilder window;

        window = new StringBuilder("*╭────┬────┬────┬────┬────╮\n");
        for (int i = 0; i < shelf.length; i++) {
            window.append("*│");
            for (int j = 0; j < shelf[i].length; j++) {
                if (shelf[i][j].isNone()) {
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

        String info = "Turno di Matteo\n";
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

    /*
    public void setGame(Player[] otherPlayers, String user, Tile[][] gameBoard, ArrayList<CommonGoalCard> commonCards) {
        int j = 0;
        this.otherPlayers = otherPlayers;
        for (int i = 0; i < otherPlayers.length; i++) {
            if (otherPlayers[i].getUsername().equals(user))
                p = otherPlayers[i];
            else {
                otherPlayers[j] = p = otherPlayers[i];
                j++;
            }
        }
        this.commonCards = commonCards;
        this.gameBoard = gameBoard;
        turnHandler = otherPlayers[0].getUsername();

    }*/
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
        System.out.println("Game Started");
        running = true;
        boolean first = true;

        while (running) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

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
                    clearScreen();
                    switch (phase) {
                        case WAIT -> {
                        }
                        case TILES_REQUEST -> {
                            paintWindow(board);
                            AskTiles();
                        }
                        case COLUMN_REQUEST -> {
                            paintWindow(board);
                            AskColumns();
                        }
                    }
                }

            }

            /*
            if (turnHandler.equals(p.getUsername())) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    connectionLost();
                }
            } else {
                turn.start();
                try {
                    wait();
                } catch (InterruptedException e) {
                    connectionLost();
                }
                turn.interrupt();
            }
            */
        }
        //TODO gestire la chiusura della partita e il calcolo del vincitore (lo calcola la view o glielo passa il server?)
    }


    /*
    /**
     * Implements the dialog between the
     *
     * @param AvailableTiles ArrayList containing all the possible combinations by 1,2 and 3 tiles that can be taken from the board
     * @return Am ArrayList containing the chosen combination
     */
    /*
    public void vPickTiles(ArrayList<ArrayList<Tile>> AvailableTiles) { //TODO Tile[][]
        int i, col;
        ArrayList<Tile> selected;
        //TODO va stampata la board per farla vedere all'utente, aspettiamo le grafiche
        System.out.println(gameBoard.toString());
        System.out.println("Which tiles do you want to take?");
        for (ArrayList<Tile> Combination : AvailableTiles) {
            System.out.println(AvailableTiles.indexOf(Combination) + ") " + Combination.toString());
        }
        i = clientInput.nextInt();
        while (!(i >= 0 && i < AvailableTiles.size())) {
            System.out.println("Unvalid Choice, try again");
            i = clientInput.nextInt();
        }
        //gameBoard.removeTiles(AvailableTiles.get(i));
        selected = AvailableTiles.get(i);
        col = vPutTiles(selected);
        AvailableTiles.clear();
        AvailableTiles.add(selected);

        //send the column chosen to the server. N.B Tiles already sent in vPutTiles
        Message response = new ColumnResponse(col);
        try {
            write(response);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    */

    private void printBoard() {
        synchronized (this) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    System.out.print(board[i][j].toString() + " ");
                }
                System.out.println(" ");
            }
            System.out.println("---------------------------------------------------");
        }
    }

    /*syncronized public Shelf getShelf() {
        return p.getShelf();
    }

    syncronized public Board getGameBoard() {
        return gameBoard;
    }

    syncronized public void setGameBoard(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    syncronized public ArrayList<CommonGoalCard> getCommonCards() {
        return commonCards;
    }

    syncronized public void setCommonCards(ArrayList<CommonGoalCard> commonCards) {
        this.commonCards = commonCards;
    }

    syncronized public PersonalGoalCard getPgc() {
        return p.pgc;
    }

    syncronized public void print_board() {
        System.out.println(gameBoard.toString());
    }

    syncronized public void print_shelf() {
        System.out.println(p.getShelf().toString());
    }


    syncronized public void print_pgc() {
        System.out.println(p.pgc.toString());
    }

    syncronized public void print_common_goal_cards() {
        System.out.println("Common Objective 1:\n" + commonCards.get(0).toString() + "\n Common Objective 2:\n" + commonCards.get(1).toString());
    }

    syncronized public void print_points() {
        System.out.println(p.getUsername() + " : " + p.getPoints() + " points");
        System.out.println("You: " + p.getPoints() + "Points");
        for (Player otherPlayer : otherPlayers) {
            System.out.println(otherPlayer.getUsername() + " :" + otherPlayer.getPoints() + "points");
        }
    }

    syncroized public void print_names() {
        System.out.println(p.getUsername());
        for (Player pl : otherPlayers)
            System.out.println(pl.getUsername());
    }
    */
    public void declare_winner(String winner) {
        System.out.println(p.getUsername() + ": " + p.getPoints() + " pts");
        for (Player pl : otherPlayers)
            System.out.println(pl.getUsername() + ": " + pl.getPoints() + " pts");
        System.out.println("The winner is: " + winner);
    }

    /*
    /**
     * @param selected Contains the tiles which have to be inserted in selection order, that is not necessarily the insertion one
     * @return the coloumn in which the player wants the tiles to be put
     */
    /*
    public int vPutTiles(ArrayList<Tile> selected) {
        int i, err = 0;
        ArrayList<Integer> indexes = new ArrayList<>();
        ArrayList<Tile> orderedTiles = new ArrayList<>();
        System.out.println("Which column do you want to insert the tiles in?");
        for (Integer k : availableColumns(selected.size())) {
            System.out.println(k.toString());
        }
        System.out.println(p.getShelfDeprecated().availableColumns(selected.size()).toString());
        i = clientInput.nextInt();
        //while (!(availableColumns(selected.size()).contains(i)))
        while (!(p.getShelfDeprecated().availableColumns(selected.size()).contains(i))) {
            System.out.println("Error: unavailable column selected");
            i = clientInput.nextInt();
        }
        System.out.println("Select the order you want to put the Tiles in");
        for (Tile T : selected)
            System.out.println(selected.indexOf(T) + ") " + T.toString());
        do {
            for (int k = 0; k < selected.size(); k++)
                indexes.add(clientInput.nextInt());
            for (int w = 0; w < selected.size() && err == 0; w++) {
                if (!(indexes.contains(w))) {
                    err = 1;
                    System.out.println("Error: invalid order specified, try again");
                }
            }
        } while (err == 1);
        for (int k = 0; k < selected.size(); k++)
            orderedTiles.add(selected.get(indexes.get(k)));
        selected.clear();
        selected.addAll(orderedTiles);

        //sends to the server the ordered list of selected tiles
        Tile[] tiles = new Tile[selected.size()];
        tiles = selected.toArray(tiles);
        Message response = new TilesResponse(tiles);
        try {
            client.write(response);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return i;
    }
    */

    public ArrayList<Integer> availableColumns(int nTiles) {
        int count;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < SHELF_COLS; i++) {
            count = 0;
            for (int j = 0, k = 0; j < SHELF_ROWS && k == 0; j++) {
                if (p.getShelf()[j][i].type.isNone())
                    count++;
                else
                    k = 1;
            }
            if (count >= nTiles)
                list.add(i);
        }

        return list;
    }

    /**
     * Asks the client if he wants to join an existing lobby or to create a new one
     *
     * @return
     */
    public void askJoinOrCreate() {
        String choice;
        System.out.println("Choose an option:\n[0] Create Lobby\n[1] Join Lobby");
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
            System.out.println("Currently there are no lobbies available\nPlease type /back to go back to the menu or /update to refresh the lobbies list!");
            return;
        }
        Logger.info("Choose a Lobby:");
        for (LobbiesList.LobbyData l : lobbiesData) {
            if (l == null) {
                break;
            } else {
                Logger.info("[" + l.id + "] " + l.admin + "'s lobby | " + l.capacity + "/4");
            }
        }
    }

    /**
     * Represents the interface's behaviour during opponents' turn, the client can look at "whatever he wants" till the main thread
     * interrupts him (when the turn has ended and the game has to be updated
     */

    public void turn() {
        paintWindow(board);
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
            System.out.println(i + comb.toString());
            i++;
        }
    }

    public HashMap<String, Tile[][]> getShelves() {
        return shelves;
    }

    public String getUsername() {
        return username;
    }
}
