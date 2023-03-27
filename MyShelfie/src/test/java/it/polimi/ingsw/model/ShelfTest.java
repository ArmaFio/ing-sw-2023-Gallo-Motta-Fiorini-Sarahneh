package it.polimi.ingsw.model;

import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.shelf.ShelfSlot;
import junit.framework.TestCase;

import java.util.ArrayList;

public class ShelfTest extends TestCase {

    public void testAvaiable_coloumns() {
        Shelf shelf = new Shelf();
        assertTrue(shelf.avaiable_coloumns(3).size() == 6);
        for (int i = 0; i < 6; i++) {
            assertEquals(shelf.avaiable_coloumns(3).get(i), i);
        }
        assertTrue(shelf.avaiable_coloumns(2).size() == 6);
        for (int i = 0; i < 6; i++) {
            assertEquals(shelf.avaiable_coloumns(2).get(i), i);
        }  assertTrue(shelf.avaiable_coloumns(1).size() == 6);
        for (int i = 0; i < 6; i++) {
            assertEquals(shelf.avaiable_coloumns(1).get(i), i);
        }
    }

    public void testAvaiable_coloumns_full() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                m[i][j] = new ShelfSlot();
                m[i][j].setTile(new Tile(TileType.BLUE));
            }
        }
        shelf.setMatrix(m);
        assertEquals(0, shelf.avaiable_coloumns(3).size());
        assertEquals(0, shelf.avaiable_coloumns(2).size());
        assertEquals(0, shelf.avaiable_coloumns(1).size());
    }

    public void testAvaiable_coloumns_general() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 5; j > 2; j--) {
                m[j][i].setTile(new Tile(TileType.BLUE));
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 5; j > 1; j--) {
                m[j][i].setTile(new Tile(TileType.BLUE));
            }
        }
        for (int i = 4; i < 5; i++) {
            for (int j = 5; j > 0; j--) {
                m[j][i].setTile(new Tile(TileType.BLUE));
            }
        }
        shelf.setMatrix(m);
        ArrayList<Integer> A = new ArrayList<Integer>();
        ArrayList<Integer> B = new ArrayList<Integer>();
        ArrayList<Integer> C = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++)
            A.add(i);
        for (int i = 0; i < 4; i++)
            B.add(i);
        for (int i = 0; i < 2; i++)
            C.add(i);
        assertEquals(C, shelf.avaiable_coloumns(3));
        assertEquals(B, shelf.avaiable_coloumns(2));
        assertEquals(C, shelf.avaiable_coloumns(1));


    }

    public void testGet_max_coloumns_empty() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        shelf.setMatrix(m);
        assertEquals(3, shelf.get_max_coloumns());
    }

    public void testGet_max_coloumns_full() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                m[i][j] = new ShelfSlot();
                m[i][j].setTile(new Tile(TileType.BLUE));
            }
        }
        shelf.setMatrix(m);
        assertEquals(0, shelf.get_max_coloumns());
    }

    public void testGet_max_coloumns_general() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 5; j >= 0; j--) {
                m[j][i].setTile(new Tile(TileType.BLUE));
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 5; j > 1; j--) {
                m[j][i].setTile(new Tile(TileType.BLUE));
            }
        }
        for (int i = 4; i < 5; i++) {
            for (int j = 5; j > 0; j--) {
                m[j][i].setTile(new Tile(TileType.BLUE));
            }
        }
        shelf.setMatrix(m);
        assertEquals(2, shelf.get_max_coloumns());
    }

    public void testPut_tiles() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        shelf.setMatrix(m);
        Tile A1 = new Tile(TileType.BLUE);
        Tile A2 = new Tile(TileType.BLUE);
        Tile A3 = new Tile(TileType.BLUE);
        ArrayList<Tile> A = new ArrayList<Tile>();
        A.add(A1);
        A.add(A2);
        A.add(A3);
        shelf.put_tiles(3, A);
        assertEquals(A1, shelf.getMatrix()[5][3].getTile());
        assertEquals(A2, shelf.getMatrix()[4][3].getTile());
        assertEquals(A3, shelf.getMatrix()[3][3].getTile());
        ArrayList<Tile> B = new ArrayList<Tile>();
        B.add(A3);
        B.add(A1);
        assertEquals(A3, shelf.getMatrix()[2][3].getTile());
        assertEquals(A1, shelf.getMatrix()[1][3].getTile());
    }

    public void testN_row() {
        Shelf shelf=new Shelf();
        assertEquals(shelf.N_ROWS, 5);
    }

    public void testN_col() {
        Shelf shelf=new Shelf();
        assertEquals(shelf.N_COLS, 6);
    }

}