package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.GameUpdate;
import it.polimi.ingsw.messages.StartMessage;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.shelf.Shelf;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller extends Thread {
    private final Game game;
    private final Lobby lobby;
    private final String[] users;
    private boolean isReceivedTiles;
    private Tile[] selectedTiles;
    private boolean isReceivedColumn;
    private int selectedColumn;
    private String currPlayer;


    public Controller(Lobby lobby, String[] users) {
        this.lobby = lobby;
        this.users = users;
        this.game = new Game(users);
        this.start();
    }


    @Override
    public void run() {
        try {
            lobby.sendStart();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (!game.isEnded()) { //end game condition
            Logger.debug("dentro al while controller");
            for (String user : users) {
                currPlayer = user;

                try {
                    lobby.sendToLobby(createUpdateMessage(currPlayer));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                isReceivedTiles = false;
                isReceivedColumn = false;

                lobby.sendAvailableTiles(currPlayer, filter(game.getAvailableTiles()));

                waitForTiles();

                lobby.sendAvailableColumns(currPlayer, game.getAvailableColumns(currPlayer, selectedTiles));

                waitForColumn();

                game.nextTurn(currPlayer, selectedTiles, selectedColumn, lobby);
                System.out.println("fine turno");

                if (game.isEnded()) {
                    StringMessage notify = new StringMessage(currPlayer + " has completed the shelf!\nThe game will end at the end of the round!");
                    try {
                        lobby.sendToLobby(notify);
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }
            }
        }

        game.endGame();

        StringMessage notify = new StringMessage("The game is over!\nThe winner is: " + game.winner + "!");
        try {
            lobby.sendToLobby(notify);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        //TODO comunica che la partita è finita (join)
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
                start = new StartMessage(p.pgc.getMatrix(), game.getCommonGoalsInfo(), p.personalId);
                return start;
            }
        }
        return null;
    }

    private synchronized Tile[][] filter(Tile[][] tiles) {
        Shelf shelf = game.getPlayer(currPlayer).getShelfObj();
        int maxTiles = shelf.get_max_columns();
        ArrayList<ArrayList<Tile>> result = new ArrayList<>();
        for (Tile[] t : tiles) {
            if (t.length <= maxTiles) {
                ArrayList<Tile> temp = new ArrayList<>(Arrays.asList(t));
                result.add(temp);
            }
        }
        return result.stream().map(e -> e.toArray(new Tile[0])).toArray(Tile[][]::new);
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
            Logger.warning("non è il tuo turno");
        }
    }


    /**
     * Called when a column is received by the player.
     * Saves the column in {@code selectedColumn} and notifies the controller.
     *
     * @param column the column selected by the {@code Player}.
     */
    public synchronized void onColumnReceived(int column) {
        if (!isReceivedColumn) {
            selectedColumn = column;
            isReceivedColumn = true;
            notifyAll();
        } else {
            Logger.warning("non è il tuo turno");
        }
    }


    /**
     * @return the {@code Player} whose turn it is
     */
    public String getCurrPlayer() {
        return this.currPlayer;
    }
}
