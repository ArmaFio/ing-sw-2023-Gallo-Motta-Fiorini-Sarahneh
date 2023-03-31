package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.shelf.ShelfSlot;
import org.junit.Assert;
import org.junit.Test;

public class CommonGoalCard_7Test {
    @Test
    public void diag_a() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 0},
                {5, 5, 5, 5, 0},
                {3, 3, 5, 0, 1},
                {5, 0, 0, 5, 1},
                {5, 1, 0, 2, 5},
                {0, 3, 3, 2, 0}
        };
        CommonGoalCard_7 goal = new CommonGoalCard_7();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            Assert.assertEquals(i, points);
        }
    }

    @Test
    public void diag_b() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 0},
                {3, 5, 5, 5, 0},
                {3, 3, 2, 0, 1},
                {5, 0, 3, 5, 1},
                {5, 1, 0, 3, 5},
                {0, 3, 3, 2, 3}
        };
        CommonGoalCard_7 goal = new CommonGoalCard_7();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            Assert.assertEquals(i, points);
        }
    }

    @Test
    public void diag_c() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 4},
                {5, 5, 5, 4, 0},
                {3, 3, 4, 0, 1},
                {5, 4, 0, 5, 1},
                {4, 1, 0, 2, 5},
                {0, 3, 3, 2, 0}
        };
        CommonGoalCard_7 goal = new CommonGoalCard_7();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            Assert.assertEquals(i, points);
        }
    }

    @Test
    public void diag_d() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 0},
                {5, 4, 5, 5, 1},
                {3, 3, 5, 1, 1},
                {5, 0, 1, 5, 1},
                {5, 1, 0, 2, 5},
                {1, 3, 3, 2, 0}
        };
        CommonGoalCard_7 goal = new CommonGoalCard_7();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            Assert.assertEquals(i, points);
        }
    }

    @Test
    public void no_diag() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 2},
                {3, 1, 5, 2, 0},
                {3, 3, 2, 0, 1},
                {5, 0, 0, 5, 1},
                {5, 0, 0, 3, 5},
                {0, 3, 3, 2, 3}
        };
        CommonGoalCard_7 goal = new CommonGoalCard_7();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            Assert.assertEquals(0, points);
        }
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