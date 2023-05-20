package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ViewGUI extends Application implements View {
    private static ViewGUI gui;
    private HashMap<Integer, HashMap<String, Integer>> commonCards;
    private boolean first;
    public String username;
    private Stage stage;
    private int boardViewed;
    private NetworkHandler client;
    private String[] lobbyUsers;
    private LobbiesList.LobbyData[] lobbiesData;
    private Tile[][] board;
    private TileType[][] personalGoal;
    private HashMap<String, Tile[][]> shelves;
    private GameState state;
    private LoginController controller;
    private CreateJoinController createJoinController;
    private InGameController inGameController;
    private GamePhase phase;
    private String currentPlayer;

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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ViewGUI.class.getResource("/main-view.fxml"));
        this.controller = new LoginController();
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        this.controller.setMainApp(this);
        stage.setTitle("MyShelfie Login");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() {
        gui = this;
        NetworkHandler.init = true;
    }

    public void setClient(NetworkHandler client) {
        this.client = client;
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
                Scene scene = new Scene(root, 600, 400);//TODO valutare di mettere direttamente dimensione schermo
                inGameController.setMainApp(this);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.setResizable(true);
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
        try {
            client.write(msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (state) {
            case LOGIN -> {
                try {
                    controller.queue.take();
                    Message response = new LoginResponse(controller.credentials[0], controller.credentials[1]);
                    try {
                        write(response);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (InterruptedException e) {
                    Logger.error("Queue error");
                }
            }
            case CREATE_JOIN -> {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.onLoginSuccess();
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
                    first = false;
                }
                switch (phase) {
                    case WAIT -> {
                        Platform.runLater(() -> {
                            inGameController.updateBoard(board);
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
        }
    }


    public void askLobby(LobbiesList.LobbyData[] lobbiesData) {
        if (lobbiesData.length == 0) {
            Platform.runLater(() -> {
                createJoinController.onEmptyLobby();
            });
            return;
        }
        Platform.runLater(() -> {
            createJoinController.onNotEmptyLobby();
            int cont = 0;
            for (LobbiesList.LobbyData l : lobbiesData) {
                if (l == null) {
                    break;
                } else {
                    createJoinController.addLobby(cont, l.admin, l.capacity, l.lobbyDim);
                    cont++;
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
                        controller.onLoginFailure();
                    }
                });
                try {
                    controller.queue.take();
                    Message response = new LoginResponse(controller.credentials[0], controller.credentials[1]);
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

    }

    @Override
    public void onAvailableColumns(int[] availableColumns) {

    }

    @Override
    public HashMap<String, Tile[][]> getShelves() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public TileType[][] getPersonalGoal() {
        return new TileType[0][];
    }

    @Override
    public void setPersonalGoal(TileType[][] personalGoal) {
        this.personalGoal = personalGoal;
    }

    @Override
    public void onGameUpdate(GameUpdate update) {
        this.currentPlayer = update.playerTurn;
        this.board = update.getBoard();
        this.shelves = update.getShelves();
        this.commonCards = update.getCommonGoals();
        this.lobbyUsers = this.shelves.keySet().toArray(new String[0]);
    }

    public String[] getLobbyUsers() {
        return lobbyUsers;
    }
}
