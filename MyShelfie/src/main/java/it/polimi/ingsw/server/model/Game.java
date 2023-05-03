package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commonGoalCards.CommonBag;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;

public class Game {
    public static final int N_TYPES = 6;
    public static final String PERSONAL_GOALS_PATH = "./src/main/java/it/polimi/ingsw/server/model/data/personalGoals.ser";
    public static final int N_PERSONAL_GOALS = 12;
    public static final int SHELF_ROWS = 6;
    public static final int SHELF_COLS = 5;
    public static final int END_GAME_TOKEN = 1;
    public static final int MAX_PLAYERS = 4;
    public static final int BOARD_DIM = 9;
    private final ArrayList<Integer> personalObjs;
    private final Player[] players;

    private final Board board;

    private final ArrayList<CommonGoalCard> commonObjs;
    public String winner;
    private boolean isEnded;
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


    /**
     * {@code Game} constructor.
     *
     * @param users {@code ArrayList} containing all the player usernames for this game.
     */
    public Game(String[] users) {
        personalObjs = new ArrayList<>();
        for (int i = 0; i < N_PERSONAL_GOALS; i++) {
            personalObjs.add(i);
        }
        players = new Player[users.length];
        for (int i = 0; i < users.length; i++) {
            players[i] = new Player(users[i], new PersonalGoalCard(personalObjs));
        }

        board = new Board(users.length, new Bag());

        commonObjs = new CommonBag().draw();
    }


    /**
     * tilePicked must be in order
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

        for (CommonGoalCard goal : commonObjs) {
            player.add_points(goal.check_objective(player.getShelfDeprecated()));
        }
    }


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

    public Player[] getPlayers() {
        return players;
    }


    public ArrayList<CommonGoalCard> getCommonObjs() {
        return commonObjs;
    }

    public Tile[][] getAvailableTiles() {
        return board.getAvailableTiles();
    }

    public int[] getAvailableColumns(String player, Tile[] selectedTiles) {
        return getPlayer(player).getAvailableColumns(selectedTiles.length);
    }

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

    public Tile[][] getBoard() {
        return board.getBoard();
    }

    public Player getPlayer(String username) {
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }

        return null;
    }
}