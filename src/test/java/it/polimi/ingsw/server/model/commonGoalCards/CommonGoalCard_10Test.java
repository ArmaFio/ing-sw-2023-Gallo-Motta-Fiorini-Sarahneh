package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.model.tiles.TileType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonGoalCard_10Test {
    @Test
    public void exactly_2_lines() {
        int[][] matrix = new int[][]{
                {5, 0, 5, 0, 0},
                {0, 5, 4, 5, 0},
                {5, 3, 4, 2, 1},
                {5, 4, 3, 2, 1},
                {0, 0, 1, 3, 3},
                {0, 6, 1, 1, 0}
        };
        CommonGoalCard_10 goal = new CommonGoalCard_10(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        Assert.assertEquals(8, points);
        points = goal.check_objective(s);
        Assert.assertEquals(0, points);
    }

    @Test
    public void more_than_2_lines() {
        int[][] matrix = new int[][]{
                {5, 0, 5, 0, 0},
                {0, 5, 4, 5, 0},
                {5, 3, 4, 3, 1},
                {5, 4, 3, 2, 1},
                {0, 0, 1, 3, 3},
                {4, 6, 1, 2, 3}
        };
        CommonGoalCard_10 goal = new CommonGoalCard_10(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        points = goal.check_objective(s);
        Assert.assertEquals(8, points);
        points = goal.check_objective(s);
        Assert.assertEquals(0, points);
    }

    @Test
    public void only_0_lines() {
        int[][] matrix = new int[][]{
                {5, 0, 5, 0, 0},
                {0, 5, 4, 5, 0},
                {5, 0, 4, 3, 1},
                {5, 4, 4, 2, 1},
                {0, 0, 1, 3, 3},
                {4, 6, 1, 1, 3}
        };
        CommonGoalCard_10 goal = new CommonGoalCard_10(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(0, points);
        }
    }

    @Test
    public void only_1_line() {
        int[][] matrix = new int[][]{
                {5, 0, 5, 0, 0},
                {0, 5, 4, 5, 0},
                {5, 3, 4, 3, 1},
                {5, 0, 3, 2, 1},
                {0, 0, 1, 3, 3},
                {4, 3, 1, 2, 6}
        };
        CommonGoalCard_10 goal = new CommonGoalCard_10(4);
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(0, points);
        }
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