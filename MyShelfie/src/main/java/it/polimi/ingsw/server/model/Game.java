package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commonGoalCards.CommonBag;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.utils.Logger;

public class Game {
    public static final int N_TYPES = 6;
    public static final String PERSONAL_GOALS_PATH = "MyShelfie/src/main/java/it/polimi/ingsw/server/model/data/personalGoals.ser";
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
    private final Player[] players;
    private final Board board;
    private final CommonGoalCard[] commonGoals;
    public String winner;
    private boolean isEnded;


    /**
     * {@code Game} constructor.
     *
     * @param users array containing all the player usernames for this game.
     */
    public Game(String[] users) {
        int[] ids = PersonalGoalCard.draw(users.length);
        Logger.debug("Personal extracted:");
        for (int i : ids) {
            System.out.println(i);
        }
        players = new Player[users.length];
        for (int i = 0; i < users.length; i++) {
            players[i] = new Player(users[i], new PersonalGoalCard(ids[i]));
        }

        board = new Board(users.length, new Bag());

        commonGoals = new CommonBag().draw();
    }


    /**
     * Runs a single turn for a {@code Player}.
     *
     * @param username    The {@code Player}'s username whose turn it is.
     * @param tilesPicked The {@code Tile}s picked by the player in order.
     * @param column      The column where the {@code Tile}s must be placed.
     */
    public void nextTurn(String username, Tile[] tilesPicked, int column) {
        Player player = getPlayer(username);

        if (player == null) {
            Logger.error("Player not found");
            //TODO exception
            return;
        }

        int length = 0;
        while (length == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            synchronized (this) {
                length = tilesPicked.length;
            }
        }

        board.removeTiles(tilesPicked);
        player.getShelfDeprecated().putTiles(column, tilesPicked);

        for (CommonGoalCard goal : commonGoals) {
            int points = goal.check_objective(player.getShelfDeprecated());
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

        for (Player p : players) {
            if (p.getShelfDeprecated().get_max_columns() == 0) {
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
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Gets the {@code CommonGoalCard}s of this {@code Game}.
     *
     * @return the {@code CommonGoalCard}s of this {@code Game}.
     */
    public int[] getCommonGoals() {
        return new int[]{commonGoals[0].getId(), commonGoals[1].getId()};
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
            p.check_objective();
            p.check_groups();
        }

        int max = players[0].getPoints();
        for (Player p : players) {
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
    public Player getPlayer(String username) {
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }

        return null;
    }
}