package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.shelf.ShelfSlot;
import org.junit.Assert;
import org.junit.Test;

public class CommonGoalCard_8Test {
    @Test
    public void exactly_4_lines() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 2},
                {3, 1, 5, 2, 0},
                {3, 3, 2, 0, 1},
                {5, 2, 2, 5, 1},
                {5, 5, 5, 3, 5},
                {2, 3, 3, 2, 3}
        };
        CommonGoalCard_8 goal = new CommonGoalCard_8();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            Assert.assertEquals(i, points);
        }
    }

    @Test
    public void more_than_4_lines() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 2},
                {3, 1, 5, 2, 0},
                {3, 3, 2, 2, 1},
                {5, 1, 1, 5, 1},
                {5, 3, 3, 3, 5},
                {2, 3, 3, 2, 3}
        };
        CommonGoalCard_8 goal = new CommonGoalCard_8();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            Assert.assertEquals(i, points);
        }
    }

    @Test
    public void less_than_4_lines() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 2},
                {3, 1, 5, 2, 0},
                {3, 3, 2, 0, 1},
                {5, 0, 0, 5, 1},
                {5, 0, 0, 3, 5},
                {0, 3, 3, 2, 3}
        };
        CommonGoalCard_8 goal = new CommonGoalCard_8();
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
                slots[i][j].setTile(new Tile(TileType.toEnum(matrix[i][j])));
            }
        }

        s.setMatrix(slots);

        return s;
    }
}