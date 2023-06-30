package it.polimi.ingsw.server.model.GameT;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.PersonalGoalCard;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.model.tiles.TileType;
import it.polimi.ingsw.utils.Logger;

import java.util.HashMap;

public class TestableGame {
    public static final int N_TYPES = TileType.values().length;
    public static final String PERSONAL_GOALS_PATH = "./personalGoals.ser";
    public static final int N_PERSONAL_GOALS = 12;
    public static final int SHELF_ROWS = 6;
    public static final int SHELF_COLS = 5;
    public static final int END_GAME_TOKEN = 1;
    public static final int MAX_PLAYERS = 4;
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
    private final TestablePlayer[] players;
    public GameTest.BoardBuilder board;
    public  CommonGoalCard[] commonGoals;
    public String winner;
    private boolean isEnded;


    /**
     * {@code Game} constructor.
     *
     * @param users An array containing all the player usernames for this game.
     */
    public TestableGame(String[] users, int id) {
        players = new TestablePlayer[users.length];
        for (int i = 0; i < users.length; i++) {
            players[i] = new TestablePlayer(users[i], new PersonalGoalCard(id));
        }
    }


    /**
     * Runs a single turn for a {@code Player}.
     *
     * @param username    The {@code Player}'s username whose turn it is.
     * @param tilesPicked The {@code Tile}s picked by the player in order.
     * @param column      The column where the {@code Tile}s must be placed.
     */
    public synchronized void nextTurn(String username, Tile[] tilesPicked, int column) {
        TestablePlayer player = getPlayer(username);

        if (player == null) {
            Logger.error("Player not found");
            return;
        }

        board.removeTiles(tilesPicked);
        player.getShelfObj().putTiles(column, tilesPicked);
        board.checkBoard();
        System.out.println("Tiles remaining in the bag: " + board.tilesBag.getTiles().size());
        //checks if a player has completed the shelf
        if (player.getShelfObj().availableColumns(1).length == 0) {
            isEnded = true;
        }

        for (CommonGoalCard goal : commonGoals) {
            int points = goal.check_objective(player.getShelfObj());
            player.add_points(points);
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

        for (TestablePlayer p : players) {
            if (p.getShelfObj().getMaxColumns() == 0) {
                p.add_points(Game.END_GAME_TOKEN);
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
    public TestablePlayer[] getPlayers() {
        return players;
    }

    /**
     * Gets the {@code CommonGoalCard}s of this {@code Game}.
     *
     * @return the {@code CommonGoalCard}s of this {@code Game}.
     */
    public HashMap<Integer, HashMap<String, Integer>> getCommonGoals() {
        HashMap<Integer, HashMap<String, Integer>> map = new HashMap<>();

        map.put(commonGoals[0].getId(), commonGoals[0].getSolvers());
        map.put(commonGoals[1].getId(), commonGoals[1].getSolvers());

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
        for (TestablePlayer p : players) {
            p.check_objective();
            p.check_groups();
        }

        int max = players[0].getPoints();
        for (TestablePlayer p : players) {
            if (max <= p.getPoints()) {
                max = p.getPoints();
                winner = p.getUsername();
            }
        }
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
    public TestablePlayer getPlayer(String username) {
        for (TestablePlayer p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }

        return null;
    }
}