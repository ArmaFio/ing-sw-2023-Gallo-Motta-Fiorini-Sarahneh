package it.polimi.ingsw.model;

import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.shelf.ShelfSlot;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ShelfTest {

    @Test
    public void testAvailable_coloumns() {
        Shelf shelf = new Shelf();
        assertEquals(5, shelf.available_columns(3).size());
        ArrayList<Integer> A = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            A.add(i);
        assertEquals(A, shelf.available_columns(3));
        assertEquals(A, shelf.available_columns(2));
        assertEquals(A, shelf.available_columns(1));
    }

    @Test
    public void testAvailable_coloumns_full() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                m[i][j] = new ShelfSlot();
                m[i][j].setTile(new Tile(TileType.CAT, 1));
            }
        }
        shelf.setMatrix(m);
        assertEquals(0, shelf.available_columns(3).size());
        assertEquals(0, shelf.available_columns(2).size());
        assertEquals(0, shelf.available_columns(1).size());
    }

    @Test
    public void testAvailable_coloumns_general() {
        Shelf shelf = new Shelf();
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
        ArrayList<Integer> A = new ArrayList<>();
        ArrayList<Integer> B = new ArrayList<>();
        ArrayList<Integer> C = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            A.add(i);
        for (int i = 0; i < 4; i++)
            B.add(i);
        for (int i = 0; i < 2; i++)
            C.add(i);
        assertEquals(C, shelf.available_columns(3));
        assertEquals(B, shelf.available_columns(2));
        assertEquals(A, shelf.available_columns(1));


    }

    @Test
    public void testGet_max_columns_empty() {
        Shelf shelf = new Shelf();
        assertEquals(3, shelf.get_max_columns());

    }

    @Test
    public void testGet_max_coloumns_full() {
        Shelf shelf = new Shelf();
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
        Shelf shelf = new Shelf();
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
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        shelf.setMatrix(m);
        Tile A1 = new Tile(TileType.CAT, 1);
        Tile A2 = new Tile(TileType.CAT, 1);
        Tile A3 = new Tile(TileType.CAT, 1);
        ArrayList<Tile> A = new ArrayList<>();
        A.add(A1);
        A.add(A2);
        A.add(A3);
        shelf.put_tiles(3, A);
        assertEquals(A1, shelf.getMatrix()[5][3].getTile());
        assertEquals(A2, shelf.getMatrix()[4][3].getTile());
        assertEquals(A3, shelf.getMatrix()[3][3].getTile());
        ArrayList<Tile> B = new ArrayList<>();
        B.add(A3);
        B.add(A1);
        shelf.put_tiles(3, B);
        assertEquals(A3, shelf.getMatrix()[2][3].getTile());
        assertEquals(A1, shelf.getMatrix()[1][3].getTile());
    }

    @Test
    public void testN_row() {
        Shelf shelf = new Shelf();
        assertEquals(shelf.N_ROWS, 6);
    }

    @Test
    public void testN_col() {
        Shelf shelf = new Shelf();
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

