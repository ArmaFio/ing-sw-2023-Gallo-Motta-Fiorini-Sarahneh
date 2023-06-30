package it.polimi.ingsw.server.model;

import it.polimi.ingsw.messages.PointsUpdate;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.model.commonGoalCards.CommonBag;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.model.tiles.TileType;
import it.polimi.ingsw.server.model.tiles.TilesBag;
import it.polimi.ingsw.utils.Logger;

import java.io.IOException;
import java.util.HashMap;

public class Game {
    public static final int N_TYPES = TileType.values().length;
    public static final String PERSONAL_GOALS_PATH = "./personalGoals.ser";
    public static final int N_PERSONAL_GOALS = 12;
    public static final int SHELF_ROWS = 6;
    public static final int SHELF_COLS = 5;
    public static final int END_GAME_TOKEN = 1;
    public static final int BOARD_DIM = 9;
    public static final int[][] boardConfiguration = new int[][]{
            {0, 0, 0, 3, 4, 0, 0, 0, 0},
            {0, 0, 0, 2, 2, 4, 0, 0, 0},
            {0, 0, 3, 2, 2, 2, 3, 0, 0},
            {0, 4, 2, 2, 2, 2, 2, 2, 3},
            {4, 2, 2, 2, 2, 2, 2, 2, 4},
            {3, 2, 2, 2, 2, 2, 2, 4, 0},
            {0, 0, 3, 2, 2, 2, 3, 0, 0},
            {0, 0, 0, 4, 2, 2, 0, 0, 0},
            {0, 0, 0, 0, 4, 3, 0, 0, 0}
    };
    public final Player[] players;
    private final Board board;
    private final CommonGoalCard[] commonGoals;
    private boolean isEnded;


    /**
     * {@code Game} constructor.
     *
     * @param users An array containing all the player usernames for this game.
     */
    public Game(String[] users) {
        int[] ids = PersonalGoalCard.draw(users.length);

        players = new Player[users.length];
        for (int i = 0; i < users.length; i++) {
            players[i] = new Player(users[i], new PersonalGoalCard(ids[i]));
        }

        board = new Board(users.length, new TilesBag());

        commonGoals = new CommonBag(users.length).draw();
    }


    /**
     * Runs a single turn for a {@code Player}.
     *
     * @param username    The {@code Player}'s username whose turn it is.
     * @param tilesPicked The {@code Tile}s picked by the player in order.
     * @param column      The column where the {@code Tile}s must be placed.
     */
    public void nextTurn(String username, Tile[] tilesPicked, int column, Lobby lobby) {
        Player player = getPlayer(username);

        if (player == null) {
            Logger.error("Player not found");
            throw new RuntimeException("Player not found");
        }

        board.removeTiles(tilesPicked);
        player.putTilesInShelf(column, tilesPicked);
        board.checkBoard();

        for (CommonGoalCard goal : commonGoals) {
            int points = goal.check_objective(player.getShelfObj());
            if (points > 0) { //TODO va tolto
                PointsUpdate message = new PointsUpdate(points, player.getUsername(), goal.getId());
                try {
                    lobby.sendToLobby(message);
                } catch (IOException e) {
                    Logger.error("unable to update points");
                }
            }
            player.addPoints(points);
        }
    }


    /**
     * Checks if the game is over.
     * If so, adds a point to the player who filled the {@code Shelf} first.
     *
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public boolean isEnded() {
        if (isEnded) {
            return true;
        }

        for (Player p : players) {
            if (p.getShelfObj().getMaxColumns() == 0) {
                p.addPoints(Game.END_GAME_TOKEN);
                isEnded = true;
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the {@code Player}s of this {@code Game}.
     *
     * @return the {@code Player}s of this {@code Game}.
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Gets the {@code CommonGoalCard}s of this {@code Game}.
     *
     * @return the {@code CommonGoalCard}s of this {@code Game}.
     */
    public HashMap<Integer, HashMap<String, Integer>> getCommonGoalsUpdate() {
        HashMap<Integer, HashMap<String, Integer>> map = new HashMap<>();

        map.put(commonGoals[0].getId(), commonGoals[0].getSolvers());
        map.put(commonGoals[1].getId(), commonGoals[1].getSolvers());

        return map;
    }

    /**
     * Gets the {@code CommonGoalCard}'s description of this {@code Game}.
     *
     * @return the {@code CommonGoalCard}'s description of this {@code Game}.
     */
    public HashMap<Integer, String> getCommonGoalsInfo() {
        HashMap<Integer, String> map = new HashMap<>();

        map.put(commonGoals[0].getId(), commonGoals[0].getDescription());
        map.put(commonGoals[1].getId(), commonGoals[1].getDescription());

        return map;
    }

    /**
     * Gets the available {@code Tile}s to be picked from the {@code Board}.
     *
     * @return the available {@code Tile}s
     */
    public Tile[][] getAvailableTiles() {
        return board.getAvailableTiles();
    }


    /**
     * Gets the available columns where the {@code Tile}s can be placed.
     *
     * @return the available columns
     */
    public int[] getAvailableColumns(String player, Tile[] selectedTiles) {
        return getPlayer(player).getAvailableColumns(selectedTiles.length);
    }


    /**
     * Called when the game is over.
     * Checks the points of each player and sets the winner.
     */
    public void endGame() {
        for (Player p : players) {
            p.checkObjective();
            p.checkGroups();
        }
    }

    /**
     * @return The winner of the game
     */
    public String getWinner() {
        int max = players[0].getPoints();
        String winner = "";

        for (Player p : players) {
            if (max <= p.getPoints()) {
                max = p.getPoints();
                winner = p.getUsername();
            }
        }
        return winner;
    }

    /**
     * Gets the {@code Board} of this {@code Game} as a matrix of {@code Tile}.
     *
     * @return the {@code Board}
     */
    public Tile[][] getBoard() {
        return board.getBoard();
    }


    /**
     * Gets the {@code Player} with the given username.
     *
     * @param username The username of the {@code Player} to get.
     * @return the {@code Player} with the given username.
     */
    public Player getPlayer(String username) {
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }

        return null;
    }
}