package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.GamePhase;
import it.polimi.ingsw.GameState;
import it.polimi.ingsw.client.CommonGoalCard;
import it.polimi.ingsw.client.NetworkHandler;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.model.tiles.TileType;
import it.polimi.ingsw.utils.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class ViewGUI extends Application implements View {
    private static ViewGUI gui;
    private final CommonGoalCard[] commonGoals = new CommonGoalCard[2];
    public String username;
    private HashMap<Integer, HashMap<String, Integer>> commonCards;
    private boolean first;
    private Stage stage;
    private int boardViewed;
    private NetworkHandler client;
    private String[] lobbyUsers;
    private LobbiesList.LobbyData[] lobbiesData;
    private Tile[][] board;
    private TileType[][] personalGoal;
    private HashMap<String, Tile[][]> shelves;
    private GameState state;
    private LoginController loginController;
    private CreateJoinController createJoinController;
    private InGameController inGameController;
    private GamePhase phase;
    private String currentPlayer;
    private boolean firstWidthHeight = true;
    private Tile[][] availableTiles;
    private int personalId;
    private ChatMessage[] chat;

    public static ViewGUI getInstance() {
        return gui;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override

    public void start(Stage stage) throws IOException {
        this.state = GameState.LOGIN;
        this.lobbiesData = new LobbiesList.LobbyData[0];
        this.lobbyUsers = new String[0];
        this.board = new Tile[0][0];
        this.shelves = new HashMap<>();
        this.boardViewed = 0;
        this.first = true;
        for (int i = 0; i < 4; i++) {
            shelves.put("none", new Tile[0][0]);
        }
        this.stage = stage;
        stage.setResizable(false);
        //stage.setMaximized(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ViewGUI.class.getResource("/main-view.fxml"));
        this.loginController = new LoginController();
        loader.setController(loginController);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        this.loginController.setMainApp(this);
        stage.setTitle("MyShelfie Login");
        //called when width is changed
        stage.widthProperty().addListener(e -> {
            if (stage.getWidth() != 0) {
                //System.out.println("called width");
                if (firstWidthHeight) {
                    loginController.resizeWidth(stage.getWidth(), 600, 400);
                    firstWidthHeight = false;
                } else {
                    loginController.resizeWidth(stage.getWidth(), 0, 0);
                }
            }
        });
        //called when height is changed
        stage.heightProperty().addListener(e -> {
            if (stage.getHeight() != 0) {
                //System.out.println("called height");
                if (firstWidthHeight) {
                    loginController.resizeHeight(stage.getHeight(), 600, 400);
                    firstWidthHeight = false;
                } else {
                    loginController.resizeHeight(stage.getHeight(), 0, 0);
                }
            }
        });
        stage.setOnCloseRequest(event -> {
            //System.out.println("closing");
            Platform.exit();
            try {
                stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() {
        gui = this;
        NetworkHandler.init = true;
    }

    @Override
    public void setClient(NetworkHandler client) {
        this.client = client;
    }

    @Override
    public void onStringRequest(StringRequest message) {
        Platform.runLater(() -> {
            inGameController.onStringRequest(message);
        });
    }

    public void changeScene(String fxml) throws IOException {
        switch (fxml) {
            case "main-view.fxml" -> {

            }
            case "/afterLogin.fxml", "/LobbyList.fxml" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                this.createJoinController = new CreateJoinController();
                loader.setController(createJoinController);
                Parent root = loader.load();
                Scene scene = new Scene(root, 600, 400);
                createJoinController.setMainApp(this);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //stage.setMaximized(true);
                        stage.setTitle("Create-Join");
                        stage.setScene(scene);
                        stage.show();
                    }
                });
            }
            case "/LobbyCreation.fxml", "/InLobby.fxml" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                loader.setController(createJoinController);
                Parent root = loader.load();
                Scene scene = new Scene(root, 600, 400);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //stage.setMaximized(true);
                        stage.setTitle("Lobby creation");
                        stage.setScene(scene);
                        stage.show();
                    }
                });
            }
            case "/InGame.fxml" -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                this.inGameController = new InGameController();
                loader.setController(inGameController);
                Parent root = loader.load();
                Scene scene = new Scene(root, 1920, 1080);
                inGameController.setMainApp(this);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.setMaximized(true);
                        stage.setResizable(false);
                        stage.setTitle("InGame");
                        stage.setScene(scene);
                        stage.show();
                    }
                });
            }
        }
    }

    @Override
    public void onLobbyListMessage(LobbiesList msg) {
        this.lobbiesData = msg.lobbiesData;
    }

    @Override
    public GameState getGameState() {
        return state;
    }

    @Override
    public synchronized void updateState(GameState newState) {
        this.state = newState;
        Message msg = new StateUpdate(this.state);
        client.write(msg);

        switch (state) {
            case LOGIN -> {
                try {
                    loginController.queue.take();
                    Message response = new LoginResponse(loginController.credentials[0], loginController.credentials[1]);
                    try {
                        write(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (InterruptedException | NullPointerException e) {
                    try {
                        wait(100);
                    } catch (InterruptedException a) {
                        Logger.error("error in queue wait");
                    }
                    updateState(GameState.LOGIN);
                }
            }
            case CREATE_JOIN -> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loginController.onLoginSuccess();
                    }
                });
                try {
                    wait(500);
                    //changing scene
                    changeScene("/afterLogin.fxml");

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            case LOBBY_CHOICE -> {
                askLobby(this.lobbiesData);
            }
            case INSIDE_LOBBY -> {
                createJoinController.updateInsideLobby();
            }
            case IN_GAME -> {
                if (first) {
                    try {
                        changeScene("/InGame.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Platform.runLater(() -> {
                        inGameController.enableShelves(lobbyUsers.length);
                    });

                    first = false;
                }
                switch (phase) {
                    case WAIT -> {
                        Platform.runLater(() -> {
                            inGameController.updateBoard(board);
                            inGameController.setPersonal(personalId);
                            inGameController.setCommonGoals(commonGoals);
                            inGameController.onStringRequest(new StringRequest(""));

                        });
                        //frame.paintWindow("Turn of " + currentPlayer, getBoardViewed(), lobbyUsers, menuValue);
                    }
                    case TILES_REQUEST -> {
                        //setBoardViewed(shelves.get() + 1);
                        //frame.paintWindow("Your turn | Digit the coordinates corresponding to the tiles you want to take! Format: B3 B4", getBoardViewed(), lobbyUsers, menuValue);
                    }
                    case COLUMN_REQUEST -> {
                        //frame.paintWindow("Your turn | Digit the character corresponding to the column you want to insert the tiles in", getBoardViewed(), lobbyUsers, menuValue);
                    }
                }
            }
            default -> {}
        }
    }


    /**
     * Gives to the player information about lobbies.
     *
     * @param lobbiesData the info about lobbies.
     */
    public void askLobby(LobbiesList.LobbyData[] lobbiesData) {
        if (lobbiesData.length == 0) {
            Platform.runLater(() -> {
                createJoinController.onEmptyLobby();
            });
            return;
        }
        Platform.runLater(() -> {
            createJoinController.onNotEmptyLobby();
            for (LobbiesList.LobbyData l : lobbiesData) {
                if (l == null) {
                    break;
                } else {
                    createJoinController.addLobby(l.id, l.admin, l.capacity, l.lobbyDim);
                }
            }
        });
    }

    @Override
    public synchronized void updateState() {
        switch (state) {
            case LOGIN -> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loginController.onLoginFailure();
                    }
                });
                try {
                    loginController.queue.take();
                    Message response = new LoginResponse(loginController.credentials[0], loginController.credentials[1]);
                    try {
                        write(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (InterruptedException e) {
                    Logger.error("Queue error");
                }
            }
            case LOBBY_CHOICE -> {
                askLobby(this.lobbiesData);
            }
            case INSIDE_LOBBY -> {
                Platform.runLater(() -> {
                    createJoinController.updateInsideLobby();
                });
            }
            case IN_GAME -> {
                switch (phase) {
                    case WAIT -> {
                        Platform.runLater(() -> {
                            inGameController.updateBoard(board);
                            inGameController.updateShelves(shelves);
                            inGameController.setCurrentPlayer(currentPlayer);
                            inGameController.onStringRequest(new StringRequest(""));
                        });
                        //frame.paintWindow("Turn of " + currentPlayer, getBoardViewed(), lobbyUsers, menuValue);
                    }
                    case TILES_REQUEST -> {
                        Platform.runLater(() -> {
                            inGameController.updateBoard(board);
                            inGameController.setCurrentPlayer(currentPlayer);
                            inGameController.setActiveTiles();
                        });
                        //setBoardViewed(shelves.get() + 1);
                        //frame.paintWindow("Your turn | Digit the coordinates corresponding to the tiles you want to take! Format: B3 B4", getBoardViewed(), lobbyUsers, menuValue);
                    }
                    case COLUMN_REQUEST -> {
                        ColumnResponse response = new ColumnResponse(inGameController.getSelectedCol());
                        try {
                            write(response);
                        } catch (IOException e) {
                            Logger.error("Cannot send column response");
                        }
                        //frame.paintWindow("Your turn | Digit the character corresponding to the column you want to insert the tiles in", getBoardViewed(), lobbyUsers, menuValue);
                    }
                }
            }
            default -> {}
        }
    }

    @Override
    public void onStringMessage(String message) {
        Platform.runLater(() -> {
            createJoinController.setLabel(message);
        });
    }

    public synchronized int askLobbyDim() {
        int lobbyDim = 0;
        while (lobbyDim == 0) {
            try {
                wait(100);
                lobbyDim = createJoinController.getSelection();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return lobbyDim;
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

    @Override
    public void updatePhase(GamePhase newPhase) {
        this.phase = newPhase;
    }

    @Override
    public void onLobbyDataMessage(String[] lobbyUsers) {
        this.lobbyUsers = lobbyUsers;
    }

    @Override
    public void write(Message message) throws IOException {
        client.write(message);
    }

    @Override
    public void onAvailableTiles(Tile[][] availableTiles) {
        this.availableTiles = availableTiles;
    }

    @Override
    public void onAvailableColumns(int[] availableColumns) {

    }

    @Override
    public HashMap<String, Tile[][]> getShelves() {
        return shelves;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public TileType[][] getPersonalGoal() {
        return new TileType[0][];
    }

    @Override
    public void setPersonalGoal(TileType[][] personalGoal, int personalId) {
        this.personalGoal = personalGoal;
        this.personalId = personalId;
    }

    @Override
    public void onGameUpdate(GameUpdate update) {
        this.currentPlayer = update.playerTurn;
        this.board = update.getBoard();
        this.shelves = update.getShelves();
        this.commonCards = update.getCommonGoals();
        this.lobbyUsers = this.shelves.keySet().toArray(new String[0]);
    }

    @Override
    public void setPassword(String s) {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void onPointsMessage(PointsUpdate points) {
        inGameController.updatePoints(points.getPoints(), points.getCommonId(), points.getPlayer());
    }

    public Tile[][] getBoard() {
        return board;
    }

    public String[] getLobbyUsers() {
        return lobbyUsers;
    }

    public Tile[][] getAvailableTiles() {
        return availableTiles;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public CommonGoalCard[] getCommonGoals() {
        return commonGoals;
    }

    @Override
    public void setCommonGoals(HashMap<Integer, String> commonsGoals) {
        int j = 0;

        for (int i : commonsGoals.keySet()) {
            this.commonGoals[j] = new CommonGoalCard(i, commonsGoals.get(i));
            j++;
        }
    }

    public int getPersonalId() {
        return personalId;
    }

    @Override
    public void onChatUpdate(ChatMessage[] chat) {
        this.chat = chat;
        if (state == GameState.IN_GAME) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    inGameController.updateChat(chat);
                }
            });
        } else if (state == GameState.INSIDE_LOBBY) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    createJoinController.updateChat(chat);
                }
            });
        }
    }

    public ChatMessage[] getChat() {
        return chat;
    }
}
