package it.polimi.ingsw.model;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.PersonalGoalCard;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.server.model.shelf.Shelf;
import it.polimi.ingsw.server.model.shelf.ShelfSlot;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersonalGoalCardTest {
    @Test
    public void nove() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {4, 0, 0, 2, 0},
                {0, 3, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 2, 0, 0}
        };
        PersonalGoalCard p = new PersonalGoalCard(1);
        Shelf s = convert_to_shelf(matrix);

        int points;

        points = p.check_objective(s);
        assertEquals(9, points);

    }

    public void dodici() {
        int[][] matrix = new int[][]{
                {6, 0, 0, 0, 0},
                {0, 0, 0, 0, 1},
                {4, 0, 0, 2, 0},
                {0, 3, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 2, 0, 0}
        };

        PersonalGoalCard p = new PersonalGoalCard(1);
        Shelf s = convert_to_shelf(matrix);

        int points;
        for(int i = 8 ; i>= 0; i-=2) {
            points = p.check_objective(s);
            assertEquals(9, points);
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
