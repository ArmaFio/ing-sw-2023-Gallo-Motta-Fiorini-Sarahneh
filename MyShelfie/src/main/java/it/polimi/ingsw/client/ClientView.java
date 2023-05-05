package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.LobbiesList;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.StateUpdate;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Armando Fiorini
 */
public class ClientView extends Thread {
    private static final int N_COLS = 5;
    private static final int N_ROWS = 6;
    private final Scanner clientInput;
    private final NetworkHandler client;
    public LobbiesList.LobbyData[] lobbiesData;//TODO private
    private InputHandler inputHandler;
    private GameState state;
    private Tile[][] gameBoard;
    private HashMap<Integer, HashMap<String, Integer>> commonCards;
    private Player p;
    private Player[] otherPlayers;
    private String turnHandler;
    private boolean running;
    private String[] lobbyUsers;
    private String currentPlayer;
    private Tile[][] board;
    private HashMap<String, Tile[][]> shelves;
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
        System.out.print("\033[H\033[2J");
        System.out.flush();
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

    /**
     * Main View's Thread, it starts when the game is running: if it's client's turn it waits for it to be completed
     * else it launches turn and interrupts it when the turnHandler has ended his turn
     */
    @Override
    public void run() {
        System.out.println("Game Started");
        running = true;
        Scanner scanner = new Scanner(System.in);
        String input;
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
                    if (lobbyUsers.length == 1) {
                        System.out.println("When you are ready type /start to begin the game");
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

    public ArrayList<Integer> availableColumns(int nTiles) {
        int count;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < N_COLS; i++) {
            count = 0;
            for (int j = 0, k = 0; j < N_ROWS && k == 0; j++) {
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

    public void askLobby(LobbiesList.LobbyData[] lobbiesData) {
        if (lobbiesData.length == 0) {
            System.out.println("Currently there are no lobbies available\nPlease type /back to go back to the menu or /update to refresh the lobbies list!");
            return;
        }
        Logger.info("Choose a Lobby:");
        for (LobbiesList.LobbyData l : lobbiesData) {
            Logger.info("[" + l.id + "] " + l.admin + "'s lobby | " + l.capacity + "/4");
        }
    }


    /**
     * @param game        updated game conditions
     * @param turnHandler next turn's handler
     */
    /*
    synchronized public void update(Game game, String turnHandler) {
        setGame(game, p.getUsername());
        this.turnHandler = turnHandler;
        notifyAll();
    }
*/

    /**
     * Represents the interface's behaviour during opponents' turn, the client can look at "whatever he wants" till the main thread
     * interrupts him (when the turn has ended and the game has to be updated
     */
    /*
    public void turn() {
        boolean loop = true;
        do {
            int i;
            System.out.println(gameBoard.toString());
            System.out.println("Menu: \n1)Show Common Objective\n2)Show Personal Objective\n3)Your Shelf\n4)Other Shelves\n");
            i = clientInput.nextInt();
            switch (i) {
                case 1 -> {
                    for (CommonGoalCard c : commonCards)
                        System.out.println(c.toString());
                    System.out.println("0) Back to menu");
                    int a;
                    do {
                        a = clientInput.nextInt();
                    } while (a != 0);
                }
                case 2 -> {
                    System.out.println(p.pgc.toString());
                    System.out.println("0) Back to menu");
                    int b;
                    do {
                        b = clientInput.nextInt();
                    } while (b != 0);
                }
                case 3 -> {
                    System.out.println(p.getShelfDeprecated().toString());
                    System.out.println("0) Back to menu");
                    int c;
                    do {
                        c = clientInput.nextInt();
                    } while (c != 0);
                }
                case 4 -> {
                    for (Player pl : otherPlayers)
                        System.out.println(p.getUsername() + "\n" + pl.getShelfDeprecated().toString());
                    System.out.println("0) Back to menu");
                    int d;
                    do {
                        d = clientInput.nextInt();
                    } while (d != 0);
                }
                default -> {
                }
            }
        } while (loop);
    }
    */
    public void welcome() {
        System.out.println("Welcome to MyShelfie!\nPlease wait while we connect you to the server!");
    }

    public void connectionEstabilished() {
        System.out.println("Connection Estabilished!");
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
}
