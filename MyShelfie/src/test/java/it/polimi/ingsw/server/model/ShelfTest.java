package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.shelf.Shelf;
import it.polimi.ingsw.server.model.shelf.ShelfSlot;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class ShelfTest {

    @Test
    public void testAvailable_coloumns() {
        Shelf shelf = new Shelf("prova");
        assertEquals(5, shelf.availableColumns(3).length);
        int[] A = {0, 1, 2, 3, 4};
        assertArrayEquals(A, shelf.availableColumns(3));
        assertArrayEquals(A, shelf.availableColumns(2));
        assertArrayEquals(A, shelf.availableColumns(1));
    }

    @Test
    public void testAvailable_coloumns_full() {
        Shelf shelf = new Shelf("prova");
        ShelfSlot[][] m = new ShelfSlot[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                m[i][j] = new ShelfSlot();
                m[i][j].setTile(new Tile(TileType.CAT, 1));
            }
        }
        shelf.setMatrix(m);
        assertEquals(0, shelf.availableColumns(3).length);
        assertEquals(0, shelf.availableColumns(2).length);
        assertEquals(0, shelf.availableColumns(1).length);
    }

    @Test
    public void testAvailable_coloumns_general() {
        Shelf shelf = new Shelf("prova");
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 5; j > 2; j--) {
                m[j][i].setTile(new Tile(TileType.CAT, 1));
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 5; j > 1; j--) {
                m[j][i].setTile(new Tile(TileType.CAT, 1));
            }
        }
        for (int i = 4; i < 5; i++) {
            for (int j = 5; j > 0; j--) {
                m[j][i].setTile(new Tile(TileType.CAT, 1));
            }
        }
        shelf.setMatrix(m);
        int[] A = {0, 1, 2, 3, 4};
        int[] B = {0, 1, 2, 3};
        int[] C = {0, 1};
        assertArrayEquals(C, shelf.availableColumns(3));
        assertArrayEquals(B, shelf.availableColumns(2));
        assertArrayEquals(A, shelf.availableColumns(1));


    }

    @Test
    public void testGet_max_columns_empty() {
        Shelf shelf = new Shelf("prova");
        assertEquals(3, shelf.get_max_columns());

    }

    @Test
    public void testGet_max_coloumns_full() {
        Shelf shelf = new Shelf("prova");
        ShelfSlot[][] m = new ShelfSlot[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                m[i][j] = new ShelfSlot();
                m[i][j].setTile(new Tile(TileType.CAT, 1));
            }
        }
        shelf.setMatrix(m);
        assertEquals(0, shelf.get_max_columns());
    }

    @Test
    public void testGet_max_coloumns_general() {
        Shelf shelf = new Shelf("prova");
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 5; j >= 0; j--) {
                m[j][i].setTile(new Tile(TileType.CAT, 1));
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 5; j > 1; j--) {
                m[j][i].setTile(new Tile(TileType.CAT, 1));
            }
        }
        for (int i = 4; i < 5; i++) {
            for (int j = 5; j > 0; j--) {
                m[j][i].setTile(new Tile(TileType.CAT, 1));
            }
        }
        shelf.setMatrix(m);
        assertEquals(2, shelf.get_max_columns());
    }

    @Test
    public void testPut_tiles() {
        Shelf shelf = new Shelf("prova");
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        shelf.setMatrix(m);
        Tile A1 = new Tile(TileType.CAT, 1);
        Tile A2 = new Tile(TileType.PLANT, 1);
        Tile A3 = new Tile(TileType.TROPHY, 1);
        Tile[] A = new Tile[]{A1, A2, A3};
        shelf.putTiles(3, A);
        assertEquals(A1, shelf.getMatrix()[5][3].getTile());
        assertEquals(A2, shelf.getMatrix()[4][3].getTile());
        assertEquals(A3, shelf.getMatrix()[3][3].getTile());
        Tile[] B = new Tile[]{A3, A1};
        shelf.putTiles(3, B);
        assertEquals(A3, shelf.getMatrix()[2][3].getTile());
        assertEquals(A1, shelf.getMatrix()[1][3].getTile());
    }

    @Test
    public void testN_row() {
        Shelf shelf = new Shelf("prova");
        assertEquals(shelf.N_ROWS, 6);
    }

    @Test
    public void testN_col() {
        Shelf shelf = new Shelf("prova");
        assertEquals(shelf.N_COLS, 5);
    }

    @Test
    public void find_groupsTest() {
        int[][] matrix = new int[][]{
                {0, 5, 4, 5, 4},
                {0, 5, 4, 4, 4},
                {3, 5, 5, 5, 5},
                {0, 3, 3, 3, 5},
                {0, 3, 0, 3, 5},
                {0, 3, 0, 3, 0}
        };
        Shelf s = convert_to_shelf(matrix);

        int[] expected = new int[]{1, 1, 5, 7, 8};
        int[] actual = s.find_groups();
        boolean found;

        assertEquals(expected.length, actual.length);

        for (int e : expected) {
            found = false;
            for (int a : actual) {
                if (e == a) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }

        for (int a : actual) {
            found = false;
            for (int e : expected) {
                if (e == a) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void find_groupsEmpty() {
        int[][] matrix = new int[][]{
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
        Shelf s = convert_to_shelf(matrix);

        int[] actual = s.find_groups();

        assertEquals(0, actual.length);
    }

    Shelf convert_to_shelf(int[][] matrix) {
        Shelf s = new Shelf("prova");
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

