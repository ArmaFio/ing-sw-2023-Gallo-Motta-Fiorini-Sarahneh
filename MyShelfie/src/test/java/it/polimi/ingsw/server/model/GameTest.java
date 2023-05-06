package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard_1;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard_2;
import org.junit.Test;

public class GameTest {
    @Test
    public void simulateGame() {
        GameBuilder game = new GameBuilder(
                new String[]{"pippo", "pluto"},
                new BoardBuilder(),
                new CommonGoalCard[]{
                        new CommonGoalCard_1(),
                        new CommonGoalCard_2()
                },
                new PersonalGoalCard[]{
                        new PersonalGoalCard(0),
                        new PersonalGoalCard(1)
                }
        );

        game.nextTurn("pippo", new Tile[]{new Tile(TileType.CAT)}, 0);

    }

    static class GameBuilder extends Game {
        private final Player[] players;
        private final Board board;
        private final CommonGoalCard[] commonGoals;
        public String winner;
        private boolean isEnded;

        public GameBuilder(String[] usernames, Board board, CommonGoalCard[] commonGoals, PersonalGoalCard[] personalGoals) {
            super(usernames);

            this.board = board;
            this.commonGoals = commonGoals;
            players = new Player[usernames.length];
            for (int i = 0; i < usernames.length; i++) {
                players[i] = new Player(usernames[i], personalGoals[i]);
            }
        }
    }

    static class BoardBuilder extends Board {
        public static final int[][] startingBoard = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 2, 0, 0, 0, 0},
                {0, 0, 0, 2, 2, 2, 0, 0, 0},
                {0, 0, 2, 2, 2, 2, 2, 2, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 0},
                {0, 2, 2, 2, 2, 2, 2, 0, 0},
                {0, 0, 0, 2, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        private final BoardSlot[][] matrix;

        public BoardBuilder() {
            super(2, new Bag());
            int k = 0;
            matrix = new BoardSlot[startingBoard.length][startingBoard.length];
            for (int i = 0; i < startingBoard.length; i++) {
                for (int j = 0; j < startingBoard[i].length; j++) {
                    if (startingBoard[i][j] == 0) {
                        matrix[i][j] = new BoardSlot(false, new Tile(TileType.toEnum(startingBoard[i][j])));
                    } else {
                        matrix[i][j] = new BoardSlot(true, new Tile(TileType.toEnum(startingBoard[i][j]), k));
                        k++;
                    }
                }
            }
        }

    }
}