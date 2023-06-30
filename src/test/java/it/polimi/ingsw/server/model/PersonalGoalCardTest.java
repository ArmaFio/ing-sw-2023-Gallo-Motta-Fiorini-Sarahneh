package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.model.tiles.TileType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonalGoalCardTest {
    @Test
    public void six() {
        int[][] matrix = new int[][]{
                {6, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {4, 0, 0, 2, 0},
                {0, 3, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0}
        };
        PersonalGoalCard p = new PersonalGoalCard(0);
        Shelf s = convert_to_shelf(matrix);

        int points;

        points = p.checkObjective(s);
        assertEquals(6, points);

    }

    @Test
    public void nine() {
        int[][] matrix = new int[][]{
                {6, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {4, 0, 0, 2, 0},
                {0, 3, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 5, 0, 0}
        };

        PersonalGoalCard p = new PersonalGoalCard(0);
        Shelf s = convert_to_shelf(matrix);

        int points;
        for (int i = 8; i >= 0; i -= 2) {
            points = p.checkObjective(s);
            assertEquals(9, points);
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
