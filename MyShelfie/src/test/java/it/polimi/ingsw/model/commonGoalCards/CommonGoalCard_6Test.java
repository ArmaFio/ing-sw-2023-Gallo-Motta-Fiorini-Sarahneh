package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard_6;
import it.polimi.ingsw.server.model.shelf.Shelf;
import it.polimi.ingsw.server.model.shelf.ShelfSlot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonGoalCard_6Test {
    @Test
    public void exactly_8_tiles() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 0},
                {5, 5, 4, 5, 0},
                {3, 3, 3, 4, 1},
                {5, 0, 6, 1, 1},
                {5, 0, 1, 2, 3},
                {4, 3, 3, 2, 6}
        };
        CommonGoalCard_6 goal = new CommonGoalCard_6();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void less_than_8_tiles() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 0},
                {5, 1, 4, 5, 0},
                {3, 3, 3, 4, 1},
                {5, 0, 6, 1, 1},
                {5, 0, 1, 2, 3},
                {4, 3, 3, 2, 6}
        };
        CommonGoalCard_6 goal = new CommonGoalCard_6();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(0, points);
        }
    }

    @Test
    public void more_than_8_tiles() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 0},
                {5, 5, 4, 5, 0},
                {3, 5, 3, 4, 1},
                {5, 0, 6, 1, 1},
                {5, 0, 1, 2, 3},
                {4, 3, 3, 2, 6}
        };
        CommonGoalCard_6 goal = new CommonGoalCard_6();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void exactly_8_tiles_of_0() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 0},
                {5, 6, 4, 5, 0},
                {3, 3, 3, 0, 1},
                {5, 0, 0, 0, 1},
                {5, 0, 0, 2, 3},
                {4, 3, 3, 2, 6}
        };
        CommonGoalCard_6 goal = new CommonGoalCard_6();
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
                slots[i][j].setTile(new Tile(TileType.toEnum(matrix[i][j])));
            }
        }

        s.setMatrix(slots);

        return s;
    }
}