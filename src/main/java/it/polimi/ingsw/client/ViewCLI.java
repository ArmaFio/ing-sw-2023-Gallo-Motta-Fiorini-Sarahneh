package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
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
    static final int HEIGHT_WINDOW = 25;
    static final int WIDTH_WINDOW = 150;
    private final NetworkHandler client;
    private String username;
    private String password;
    public LobbiesList.LobbyData[] lobbiesData;
    private final InputHandler inputHandler;
    private GameState state;
    private GamePhase phase;
    private final CommonGoalCard[] commonGoals;
    private TileType[][] personalGoal;
    private boolean running;
    private String[] lobbyUsers;
    private String currentPlayer;
    private Tile[][] board;
    private HashMap<String, Tile[][]> shelves;
    private Tile[][] availableTiles;
    private int[] availableColumns;
    private final FrameCLI frame;
    private int menuValue;
    private int boardViewed;
    private String inputMsg;
    private int personalId;
    private ChatMessage[] chat;


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
        this.boardViewed = 1;
        this.commonGoals = new CommonGoalCard[2];
        this.username = "";
        this.password = "";
        this.inputMsg = "";
        for (int i = 0; i < 4; i++) {
            shelves.put("none", new Tile[0][0]);
        }
        this.chat = new ChatMessage[]{};
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
        running = true;
        synchronized (this) {
            while (running) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    switch (state) {
                        case LOGIN -> {
                            frame.paintWindow("Enter your credentials │ " + inputMsg, username, password);
                            frame.paintWindow("Enter your credentials │ " + inputMsg, username, password);
                            setMessage("");
                        }
                        case CREATE_JOIN -> {
                            frame.paintWindow("Choose an option │ " + inputMsg);
                            setMessage("");
                        }
                        case LOBBY_CHOICE -> {
                            frame.paintWindow("Choose an option │ " + inputMsg, this.lobbiesData);
                            setMessage("");
                        }
                        case INSIDE_LOBBY ->
                                frame.paintWindow("Waiting for the start of the game │ " + inputMsg, lobbyUsers, isAdmin());
                        case IN_GAME -> {
                            switch (phase) {
                                case WAIT -> {
                                    if (this.boardViewed != 0) {
                                        frame.paintWindow("Turn of " + currentPlayer + "│" + inputMsg, getBoardViewed(), lobbyUsers, menuValue);
                                    } else {
                                        frame.paintWindow("Turn of " + currentPlayer + "│" + inputMsg, convertToTile(personalGoal), new String[]{commonGoals[0].description, commonGoals[1].description}, lobbyUsers, menuValue);
                                    }
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
                                    if (!inputMsg.equals("")) {
                                        setMessage("Digit the coordinates corresponding to the tiles you want to take");
                                    }
                                    if (this.boardViewed != 0) {
                                        frame.paintWindow("Your turn │ " + inputMsg, getBoardViewed(), lobbyUsers, menuValue);
                                    } else {
                                        frame.paintWindow("Your turn │ " + inputMsg, convertToTile(personalGoal), new String[]{commonGoals[0].description, commonGoals[1].description}, lobbyUsers, menuValue);
                                    }

                                    setMessage("");
                                }
                                case COLUMN_REQUEST -> {
                                    if (!inputMsg.equals("")) {
                                        setMessage("Digit the character corresponding to the column you want to insert the tiles in");
                                    }

                                    if (this.boardViewed != 0) {
                                        frame.paintWindow("Your turn │ " + inputMsg, getBoardViewed(), lobbyUsers, menuValue);
                                    } else {
                                        frame.paintWindow("Your turn │ " + inputMsg, convertToTile(personalGoal), new String[]{commonGoals[0].description, commonGoals[1].description}, lobbyUsers, menuValue);
                                    }
                                    setMessage("");
                                }
                            }
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    System.out.println(e);
                }
            }
        }//TODO gestire la chiusura della partita e il calcolo del vincitore (lo calcola la view o glielo passa il server?)}
    }

    private boolean isAdmin() {
        if (lobbyUsers.length > 0){
            return lobbyUsers[0].equals(username);
        }
        return true;
    }

    private String[] getCommonsDescription() {
        return new String[]{commonGoals[0].description, commonGoals[1].description};
    }

    private String mainMenu() {
        return "  __  ____     __   _____ _    _ ______ _      ______ _____ ______ \n" +
                " |  \\/  \\ \\   / /  / ____| |  | |  ____| |    |  ____|_   _|  ____|\n" +
                " | \\  / |\\ \\_/ /  | (___ | |__| | |__  | |    | |__    | | | |__   \n" +
                " | |\\/| | \\   /    \\___ \\|  __  |  __| | |    |  __|   | | |  __|  \n" +
                " | |  | |  | |     ____) | |  | | |____| |____| |     _| |_| |____ \n" +
                " |_|  |_|  |_|    |_____/|_|  |_|______|______|_|    |_____|______|\n" +
                "                                                                   \n" +
                "                                                                   ";
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
            client.write(msg);
        this.notifyAll();
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setClient(NetworkHandler client) {

    }

    @Override
    public void onStringRequest(StringRequest message) {
        System.out.println(message.message());
    }

    public synchronized void updateState() {
        this.notifyAll();
    }

    public synchronized void updatePhase(GamePhase newPhase) {
        this.phase = newPhase;
        if (this.phase == GamePhase.TILES_REQUEST) {
            this.boardViewed = 1;
        } else if (this.phase == GamePhase.COLUMN_REQUEST) {
            for(int i = 0; i < lobbyUsers.length; i++){
                if (lobbyUsers[i].equals(username)){
                    this.boardViewed = i + 2;
                }
            }
        }
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
                Logger.info("[" + cont + "] " + l.admin + "'s lobby │ " + l.capacity + "/" + l.lobbyDim);
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
    public void write(Message message) {
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
        this.commonGoals[0].updateSolvers(update.getCommonGoals().get(this.commonGoals[0].id));
        this.commonGoals[0].updateSolvers(update.getCommonGoals().get(this.commonGoals[1].id));
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

    public void setPersonalGoal(TileType[][] personalGoal, int personalId) {
        this.personalGoal = personalGoal;
    }

    public void setCommonGoals(HashMap<Integer, String> commonsGoals) {
        int j = 0;

        for(int i : commonsGoals.keySet()){
            this.commonGoals[j] = new CommonGoalCard(i, commonsGoals.get(i));
            j++;
        }
    }

    public int getMenuValue() {
        return this.menuValue;
    }

    public void setMenuValue(int choice) {
        if (choice >= -1 && choice <= 2) {
            if(choice == this.menuValue){
                this.menuValue = -1;
            } else{
                this.menuValue = choice;
            }
        }
    }

    private Tile[][] getBoardViewed() {
        int i = 2;

        for (String player : shelves.keySet()) {
            if (this.boardViewed == i) {
                return shelves.get(player);
            }
            i++;
        }

        return this.board;
    }

    public Tile[][] convertToTile(TileType[][] tiles){
        Tile[][] m = new Tile[tiles.length][tiles[0].length];

        for(int i = 0; i < m.length; i++){
            for(int j = 0; j < m[0].length; j++){
                m[i][j] = new Tile(tiles[i][j]);
            }
        }

        return m;
    }

    public void setBoardViewed(int choice) {
        if (choice >= 0 && choice <= shelves.size() + 1) {
            this.boardViewed = choice;
        }
    }

    public synchronized void setMessage(String msg) {
        this.inputMsg = msg;
    }

    public void onStringMessage(String message) {
        System.out.println(message);
    }

    public Tile getTileFromBoard(int i, int j) {
        return this.board[this.board.length - i - 1][j];
    }

    boolean isUsernameSet() {
        return !this.username.equals("");
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    boolean isPasswordSet() {
        return !this.password.equals("");
    }

    @Override
    public void disconnect() {
        client.disconnect();
        inputHandler.disconnect();
        running = false;
    }

    @Override
    public void onPointsMessage(PointsUpdate points) {

    }

    public synchronized void onChatUpdate(ChatMessage[] chat) {
        this.chat = chat;
        frame.setChat(chat);
        if (menuValue == 1) {
            this.notifyAll();
        }
    }

    public int getNumPlayers() {
        return lobbyUsers.length;
    }

    public void sendMessage(String input) {
        String receiver = null;
        for (String user : lobbyUsers) {
            if (user.equals(input.split(" ")[1]) && !user.equals(getUsername())) {
                receiver = user;
            }
        }

        StringBuilder msg = new StringBuilder();

        for (int i = (receiver == null ? 1 : 2); i < input.split(" ").length; i++) {
            msg.append(input.split(" ")[i]).append(" ");
        }

        if (receiver == null) {
            write(new ChatMessage(msg.toString()));
        } else {
            write(new ChatMessage(receiver, msg.toString()));
        }
    }
}
