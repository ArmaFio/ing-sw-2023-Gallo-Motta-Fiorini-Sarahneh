package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.shelf.ShelfSlot;
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
                {0, 6, 1, 1, 0},
        };
        CommonGoalCard_10 goal = new CommonGoalCard_10();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void more_than_2_lines() {
        int[][] matrix = new int[][]{
                {5, 0, 5, 0, 0},
                {0, 5, 4, 5, 0},
                {5, 3, 4, 3, 1},
                {5, 4, 3, 2, 1},
                {0, 0, 1, 3, 3},
                {4, 6, 1, 2, 3},
        };
        CommonGoalCard_10 goal = new CommonGoalCard_10();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void only_0_lines() {
        int[][] matrix = new int[][]{
                {5, 0, 5, 0, 0},
                {0, 5, 4, 5, 0},
                {5, 0, 4, 3, 1},
                {5, 4, 4, 2, 1},
                {0, 0, 1, 3, 3},
                {4, 6, 1, 1, 3},
        };
        CommonGoalCard_10 goal = new CommonGoalCard_10();
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
                {4, 3, 1, 2, 6},
        };
        CommonGoalCard_10 goal = new CommonGoalCard_10();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(0, points);
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