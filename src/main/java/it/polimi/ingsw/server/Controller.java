package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.GameUpdate;
import it.polimi.ingsw.messages.StartMessage;
import it.polimi.ingsw.messages.StringRequest;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.network.ClientHandler;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;

public class Controller extends Thread {
    private final Game game;
    private final Lobby lobby;
    private final String[] users;
    private final Object lock = new Object();
    private boolean isReceivedTiles;
    private Tile[] selectedTiles;
    private boolean isReceivedColumn;
    private String currPlayer;


    public Controller(Lobby lobby, String[] users) {
        this.lobby = lobby;
        this.users = users;
        this.game = new Game(users);
        this.start();
    }


    @Override
    public synchronized void run() {
        try {
            lobby.sendStart();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (!game.isEnded()) { //end game condition
            for (String user : users) {
                if (lobby.nConnectedUsers() == 0)
                    interrupt();
                if (lobby.nConnectedUsers() == 1) {
                    synchronized (lock) {
                        waitForOthers();
                    }
                }
                if (lobby.userConnected(user)) {
                    currPlayer = user;
                    try {
                        lobby.sendToLobby(createUpdateMessage(currPlayer));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    isReceivedTiles = false;
                    isReceivedColumn = false;
                    if (lobby.userConnected(currPlayer)) {
                        lobby.sendAvailableTiles(currPlayer, game.getPlayer(currPlayer).filter(game.getAvailableTiles()));
                        waitForTiles();
                    }

                    if (lobby.userConnected(currPlayer)) {
                        lobby.sendAvailableColumns(currPlayer, game.getAvailableColumns(currPlayer, selectedTiles));
                        waitForColumn();
                    }

                }
                if (game.isEnded()) {
                    StringRequest notify = new StringRequest(currPlayer + " has completed the shelf!\nThe game will end at the end of the round!");
                    try {
                        lobby.sendToLobby(notify);
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }
            }
        }

        game.endGame();

        StringRequest notify = new StringRequest("The game is over!\nThe winner is: " + game.getWinner() + "!");
        try {
            lobby.sendToLobby(notify);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Creates a start message for the given player.
     *
     * @param player the player who wants to create the start message.
     * @return the start message.
     */
    public StartMessage createStart(String player) {
        StartMessage start;
        for (Player p : game.getPlayers()) {
            if (p.getUsername().equals(player)) {
                start = new StartMessage(p.personalGoalCard.getMatrix(), game.getCommonGoalsInfo(), p.personalGoalCard.id);
                return start;
            }
        }
        return null;
    }


    /**
     * Waits for the column selected by the {@code Player}.
     */
    private synchronized void waitForColumn() {
        while (!isReceivedColumn) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Waits for the {@code Tile} selected by the {@code Player}.
     */
    private synchronized void waitForTiles() {
        while (!isReceivedTiles) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Creates a {@code Message updateGame} containing the model's updates to send to the {@code Lobby}.
     *
     * @param player the {@code Player} whose turn it is.
     * @return the {@code Message} to send to the {@code Lobby}.
     */
    private GameUpdate createUpdateMessage(String player) {
        GameUpdate msg = new GameUpdate(player);

        msg.setBoard(game.getBoard());

        for (Player p : game.getPlayers()) {
            msg.addShelf(p.getUsername(), p.getShelf());
        }

        msg.setCommonGoals(game.getCommonGoalsUpdate());


        return msg;


    }


    /**
     * Called when the {@code Tile} selected are received by the {@code Player}.
     * Saves the tile in {@code selectedTiles} and notifies the controller.
     *
     * @param tiles the {@code Tile} selected by the {@code Player}.
     */
    public synchronized void onTileReceived(Tile[] tiles) {
        if (!isReceivedTiles) {
            selectedTiles = tiles;
            isReceivedTiles = true;
            notifyAll();
        } else {
            Logger.warning("Not your turn");
        }
    }


    /**
     * Called when a column is received by the player.
     * Saves the column in {@code selectedColumn} and notifies the controller.
     *
     * @param column the column selected by the {@code Player}.
     */
    public synchronized void onColumnReceived(int column) throws IOException {
        if (!isReceivedColumn) {
            isReceivedColumn = true;
            game.nextTurn(currPlayer, selectedTiles, column, lobby);
            if (lobby.nConnectedUsers() == 1)
                lobby.sendToLobby(createUpdateMessage(currPlayer));
            notifyAll();
        } else {
            Logger.warning("Not your turn");
        }
    }


    /**
     * @return the {@code Player} whose turn it is
     */
    public String getCurrPlayer() {
        return this.currPlayer;
    }

    public void onClientSwitched(ClientHandler c) throws IOException {
        c.send(createUpdateMessage(currPlayer));
    }

    public synchronized void skip(String username) {
        synchronized (lock) {
            if (currPlayer.equals(username)) {
                if (lobby.nConnectedUsers() == 1) {
                    waitForOthers();
                    if (lobby.userConnected(currPlayer)) {
                        if (!isReceivedTiles)
                            lobby.sendAvailableTiles(currPlayer, game.getPlayer(currPlayer).filter(game.getAvailableTiles()));
                        else if (!isReceivedColumn)
                            lobby.sendAvailableColumns(currPlayer, game.getAvailableColumns(currPlayer, selectedTiles));
                    } else {
                        isReceivedTiles = true;
                        isReceivedColumn = true;
                        this.notifyAll();
                    }
                } else {
                    isReceivedTiles = true;
                    isReceivedColumn = true;
                    this.notifyAll();
                }
            }
        }
    }

    public void awake() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void waitForOthers() {
        try {
            lobby.sendToLobby(new StringRequest("Not enough players, waiting for anyone to reconnect"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lobby.timer();
        try {
            lock.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
