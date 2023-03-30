package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.shelf.ShelfSlot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonGoalCard_12Test {
    @Test
    public void decreasingRight_1() {
        int[][] matrix = new int[][]{
                {1, 0, 0, 0, 0},
                {2, 3, 0, 0, 0},
                {1, 5, 3, 0, 0},
                {3, 5, 1, 1, 0},
                {4, 3, 1, 3, 3},
                {3, 6, 1, 1, 1},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void decreasingRight_2() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0},
                {1, 5, 0, 0, 0},
                {3, 5, 1, 0, 0},
                {4, 3, 1, 3, 0},
                {3, 6, 1, 1, 1},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void increasingRight_1() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 3},
                {0, 0, 0, 1, 5},
                {0, 0, 3, 1, 1},
                {0, 6, 1, 3, 3},
                {5, 6, 1, 1, 5},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void increasingRight_2() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 1},
                {0, 0, 0, 4, 3},
                {0, 0, 3, 1, 5},
                {0, 1, 3, 1, 1},
                {4, 6, 1, 3, 3},
                {5, 6, 1, 1, 5},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void decreasingWrong_1() {
        int[][] matrix = new int[][]{
                {1, 0, 0, 0, 0},
                {2, 3, 0, 0, 0},
                {1, 5, 0, 0, 0},
                {3, 5, 1, 1, 0},
                {4, 3, 1, 3, 3},
                {3, 6, 1, 1, 1},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        assertEquals(0, points);
    }

    @Test
    public void decreasingWrong_2() {
        int[][] matrix = new int[][]{
                {1, 0, 0, 0, 0},
                {2, 3, 0, 0, 0},
                {1, 5, 1, 0, 0},
                {3, 5, 1, 1, 0},
                {4, 3, 1, 3, 3},
                {3, 6, 1, 1, 0},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        assertEquals(0, points);
    }

    @Test
    public void decreasingWrong_3() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {3, 5, 0, 0, 0},
                {4, 3, 1, 0, 0},
                {3, 6, 1, 2, 0},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        assertEquals(0, points);
    }

    @Test
    public void decreasingWrong_4() {
        int[][] matrix = new int[][]{
                {1, 5, 0, 0, 0},
                {2, 3, 6, 0, 0},
                {1, 5, 1, 3, 0},
                {3, 5, 1, 1, 1},
                {4, 3, 1, 3, 3},
                {3, 6, 1, 1, 0},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        assertEquals(0, points);
    }

    @Test
    public void increasingWrong_1() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 5},
                {0, 0, 0, 1, 1},
                {0, 0, 1, 3, 3},
                {0, 6, 1, 1, 0},
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12();
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        assertEquals(0, points);
    }

    Shelf convert_to_shelf(int[][] matrix) {
        Shelf s = new Shelf();
        ShelfSlot[][] slots = new ShelfSlot[Game.SHELF_ROWS][Game.SHELF_COLS];

        for (int i = 0; i < Game.SHELF_ROWS; i++) {
            for (int j = 0; j < Game.SHELF_COLS; j++) {
                slots[i][j] = new ShelfSlot();
                slots[i][j].setTile(new Tile(TileType.getEnum(matrix[i][j])));
            }
        }

        s.setMatrix(slots);

        return s;
    }
}