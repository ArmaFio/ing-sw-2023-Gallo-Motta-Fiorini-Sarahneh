package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard_11;
import it.polimi.ingsw.server.model.shelf.Shelf;
import it.polimi.ingsw.server.model.shelf.ShelfSlot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonGoalCard_11Test {
    @Test
    public void Right() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 5, 4, 5, 0},
                {0, 0, 5, 4, 5},
                {0, 5, 3, 5, 1},
                {0, 0, 1, 3, 3},
                {0, 6, 1, 1, 0}
        };
        CommonGoalCard_11 goal = new CommonGoalCard_11();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void X_and_other_tiles() {
        int[][] matrix = new int[][]{
                {5, 0, 5, 0, 0},
                {5, 5, 4, 5, 0},
                {5, 0, 5, 4, 5},
                {0, 4, 3, 5, 1},
                {0, 0, 1, 3, 3},
                {0, 6, 1, 1, 0}
        }; //TODO Giusto o sbagliato?
        CommonGoalCard_11 goal = new CommonGoalCard_11();
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = goal.check_objective(s);
            assertEquals(i, points);
        }
    }

    @Test
    public void X_not_complete() {
        int[][] matrix = new int[][]{
                {5, 0, 0, 0, 0},
                {0, 5, 4, 5, 0},
                {5, 0, 5, 4, 5},
                {0, 4, 3, 5, 1},
                {0, 0, 1, 3, 3},
                {0, 6, 1, 1, 0}
        };
        CommonGoalCard_11 goal = new CommonGoalCard_11();
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