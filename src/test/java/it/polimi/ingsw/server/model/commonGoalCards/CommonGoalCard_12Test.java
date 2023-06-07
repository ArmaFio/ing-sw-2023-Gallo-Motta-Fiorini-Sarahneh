package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.server.model.shelf.Shelf;
import org.junit.Assert;
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
                {3, 6, 1, 1, 1}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        Assert.assertEquals(8, points);
        points = goal.check_objective(s);
        Assert.assertEquals(0, points);
    }

    @Test
    public void decreasingRight_2() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {2, 0, 0, 0, 0},
                {1, 5, 0, 0, 0},
                {3, 5, 1, 0, 0},
                {4, 3, 1, 3, 0},
                {3, 6, 1, 1, 1}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        Assert.assertEquals(8, points);
        points = goal.check_objective(s);
        Assert.assertEquals(0, points);
    }

    @Test
    public void increasingRight_1() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 3},
                {0, 0, 0, 1, 5},
                {0, 0, 3, 1, 1},
                {0, 6, 1, 3, 3},
                {5, 6, 1, 1, 5}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        Assert.assertEquals(8, points);
        points = goal.check_objective(s);
        Assert.assertEquals(0, points);
    }

    @Test
    public void increasingRight_2() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 1},
                {0, 0, 0, 4, 3},
                {0, 0, 3, 1, 5},
                {0, 1, 3, 1, 1},
                {4, 6, 1, 3, 3},
                {5, 6, 1, 1, 5}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        Assert.assertEquals(8, points);
        points = goal.check_objective(s);
        Assert.assertEquals(0, points);
    }

    @Test
    public void decreasingWrong_1() {
        int[][] matrix = new int[][]{
                {1, 0, 0, 0, 0},
                {2, 3, 0, 0, 0},
                {1, 5, 0, 0, 0},
                {3, 5, 1, 1, 0},
                {4, 3, 1, 3, 3},
                {3, 6, 1, 1, 1}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
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
                {3, 6, 1, 1, 0}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
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
                {3, 6, 1, 2, 0}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
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
                {3, 6, 1, 1, 0}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
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
                {0, 6, 1, 1, 0}
        };
        CommonGoalCard_12 goal = new CommonGoalCard_12(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        assertEquals(0, points);
    }

    Shelf convert_to_shelf(int[][] matrix) {
        Shelf s = new Shelf("Test");
        Tile[][] slots = new Tile[Game.SHELF_ROWS][Game.SHELF_COLS];

        for (int i = 0; i < Game.SHELF_ROWS; i++) {
            for (int j = 0; j < Game.SHELF_COLS; j++) {
                slots[i][j] = new Tile(TileType.toEnum(matrix[i][j] + 1));
            }
        }

        s.setMatrix(slots);

        return s;
    }
}