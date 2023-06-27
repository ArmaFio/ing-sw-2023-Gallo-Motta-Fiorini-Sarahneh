package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.GamePhase;
import it.polimi.ingsw.utils.Logger;
import javafx.application.Application;

import java.io.IOException;

public abstract class NetworkHandler {
    public static boolean init = false;
    protected boolean running = true;
    protected String username;
    protected View view;

    protected NetworkHandler(int choice) {
        if (choice == 0) {
            view = new ViewCLI(this);
        } else {
            Thread initialize = new Thread(() -> Application.launch(ViewGUI.class));
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
    }

    protected void clientStart() {
        running = true;
        while (running) {
            Message message = read();
            if (message.getType() == MessageType.STATE_UPD)
                view.updateState(((StateUpdate) message).newState);
            else {
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
                            case JOIN_FAILURE -> view.updateState(GameState.CREATE_JOIN);
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
                            case JOIN_FAILURE -> view.updateState(GameState.CREATE_JOIN);
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
                                StringMessage notify = (StringMessage) message;
                                view.onStringMessage(notify.message());
                            }
                            case CHAT -> view.onChatUpdate(((Chat) message).getMessages());
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");

                        }
                    }
                    case IN_GAME -> {
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
                                StringMessage notify = (StringMessage) message;
                                view.onStringRequest(notify);
                            }
                            case POINTS -> {
                                PointsUpdate points = (PointsUpdate) message;
                                view.onPointsMessage(points);
                            }
                            case CHAT -> view.onChatUpdate(((Chat) message).getMessages());
                            default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");
                        }
                    }
                    default -> Logger.warning("Message " + message.getType().toString() + " not accepted!");
                }
            }

        }
    }

    abstract void write(Message m) throws IOException;

    abstract void disconnect();

    abstract Message read();

    abstract void setUsername(String Message);
}
