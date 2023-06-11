package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.MainServerRMInterface;
import it.polimi.ingsw.RMInterface;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.Logger;
import javafx.application.Application;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;

public class RMI_NetworkHandler extends NetworkHandler implements Remote, Serializable {
    public static final String ANSIRed = "\u001B[31m";
    public static final String ANSIReset = "\u001B[0m";
    private final boolean first = true;
    private final View view;
    private boolean running;
    private String username;
    private int nThread;
    private RMInterface rmi;
    private RMInterface remoteClient;

    public RMI_NetworkHandler(int choice) throws IOException, NotBoundException {
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
        nThread=-1;
        remoteClient=new RMInterfaceCImpl(this);
        connect();
        start();
        //start listening for server instructions
    }

    public void run(){
        running = true;
        while (running) {
            synchronized (this){
                try {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Message message = remoteClient.getMessage();
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
                                    view.setPersonalGoal(((StartMessage) message).getPersonalGoal(), ((StartMessage) message).personalId);
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
                                case POINTS -> {
                                    PointsUpdate points = (PointsUpdate) message;
                                    view.onPointsMessage(points);
                                }
                                default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");
                            }
                        }
                        default -> Logger.warning("messaggio ignorato");
                    }
                } catch (RemoteException e) {
                    System.out.println(e);
                }
            }
    }

}


    @Override
    public void write(Message x) throws IOException {
        rmi.write(x);
    }

    @Override
    public void disconnect() {
        view.updateState(GameState.CLOSE);
        view.disconnect();
    }

    @Override
    public void reconnect() {
        view.updateState(GameState.CLOSE);
        view.updateState(GameState.LOGIN);
        rmi = null;
        connect();
    }

    public void connect() {
        boolean firstTime = true;
        Registry registry = null;
        while (registry == null) {
            try {
                registry = LocateRegistry.getRegistry(1099);
            } catch (RemoteException e) {
                System.out.println("Cannot Find RMI Registry, retrying...");
            }
        }
        while (nThread == -1) {
            try {
                MainServerRMInterface server = (MainServerRMInterface) registry.lookup("MainServer");
                nThread = server.connect();
            } catch (RemoteException | NotBoundException e) {
                if (firstTime) {
                    System.out.println("Cannot connect to the server, keep trying...");
                    firstTime = false;
                }
            }
            if (nThread == -1) {
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
        System.out.println("Connection with the Main Server Estabilished!");
        try {
            this.rmi = (RMInterface) registry.lookup(nThread + "RMInterface");
            rmi.selfSend(remoteClient);
            connectionSignal();
        } catch (RemoteException | NotBoundException | ServerNotActiveException | InterruptedException e) {
            System.out.println("Cannot find personal rmi connection" + e);
        }
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
        view.setUsername(username);
    }

    public synchronized void update(Message m) {
        this.notifyAll();
    }

    public void connectionSignal() {
        new Thread(() -> {
            while (view.getGameState() != GameState.CLOSE) {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    rmi.ping();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}

