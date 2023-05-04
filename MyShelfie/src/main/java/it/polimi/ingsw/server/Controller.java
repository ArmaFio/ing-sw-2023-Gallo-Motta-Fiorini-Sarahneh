package it.polimi.ingsw.server;

import it.polimi.ingsw.messages.UpdateGame;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;

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

        while (game.isEnded()) { //end game condition
            for (String user : users) {
                currPlayer = user;

                try {
                    lobby.sendToLobby(createUpdateMessage(currPlayer));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                isReceivedTiles = false;
                isReceivedColumn = false;

                lobby.sendAvailableTiles(currPlayer, game.getAvailableTiles());

                while (!isReceivedTiles) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                lobby.sendAvailableColumns(currPlayer, game.getAvailableColumns(currPlayer, selectedTiles));

                while (!isReceivedColumn) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                game.nextTurn(currPlayer, selectedTiles, selectedColumn);

                if (game.isEnded()) {
                    //TODO player ha terminato la board
                }
            }
        }

        game.endGame();

        //TODO comunica al controller che la partita è finita (join)
    }


    /**
     * Creates a {@code Message updateGame} containing the model's updates to send to the {@code Lobby}.
     *
     * @param player the {@code Player} whose turn it is.
     * @return the {@code Message} to send to the {@code Lobby}.
     */
    private UpdateGame createUpdateMessage(String player) {
        UpdateGame msg = new UpdateGame(player);

        msg.setBoard(game.getBoard());

        for (Player p : game.getPlayers()) {
            msg.addShelf(p.getUsername(), p.getShelf());
        }

        msg.setCommonGoals(game.getCommonGoals());

        return msg;
    }


    /**
     * Called when the {@code Tile} selected are received by the {@code Player}.
     * Saves the tile in {@code selectedTiles} and notifies the controller.
     *
     * @param tiles the {@code Tile} selected by the {@code Player}.
     */
    public void onTileReceived(Tile[] tiles) {
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
    public void onColumnReceived(int column) {
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
