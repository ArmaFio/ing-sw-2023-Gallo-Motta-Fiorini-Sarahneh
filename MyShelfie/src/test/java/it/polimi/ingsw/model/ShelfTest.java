package it.polimi.ingsw.model;

import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.shelf.ShelfSlot;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ShelfTest {

    @Test
    public void testAvaiable_coloumns() {
        Shelf shelf = new Shelf();
        assertEquals(5, shelf.avaiable_coloumns(3).size());
        ArrayList<Integer> A = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            A.add(i);
        assertEquals(A, shelf.avaiable_coloumns(3));
        assertEquals(A, shelf.avaiable_coloumns(2));
        assertEquals(A, shelf.avaiable_coloumns(1));
    }

    @Test
    public void testAvaiable_coloumns_full() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                m[i][j] = new ShelfSlot();
                m[i][j].setTile(new Tile(TileType.CAT));
            }
        }
        shelf.setMatrix(m);
        assertEquals(0, shelf.avaiable_coloumns(3).size());
        assertEquals(0, shelf.avaiable_coloumns(2).size());
        assertEquals(0, shelf.avaiable_coloumns(1).size());
    }

    @Test
    public void testAvaiable_coloumns_general() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++)
                m[i][j] = new ShelfSlot();
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 5; j > 2; j--) {
                m[j][i].setTile(new Tile(TileType.CAT));
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 5; j > 1; j--) {
                m[j][i].setTile(new Tile(TileType.CAT));
            }
        }
        for (int i = 4; i < 5; i++) {
            for (int j = 5; j > 0; j--) {
                m[j][i].setTile(new Tile(TileType.CAT));
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
        assertEquals(C, shelf.avaiable_coloumns(3));
        assertEquals(B, shelf.avaiable_coloumns(2));
        assertEquals(A, shelf.avaiable_coloumns(1));


    }

    @Test
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

    @Test
    public void testGet_max_coloumns_full() {
        Shelf shelf = new Shelf();
        ShelfSlot[][] m = new ShelfSlot[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                m[i][j] = new ShelfSlot();
                m[i][j].setTile(new Tile(TileType.CAT));
            }
        }
        shelf.setMatrix(m);
        assertEquals(0, shelf.get_max_coloumns());
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
                m[j][i].setTile(new Tile(TileType.CAT));
            }
        }
        for (int i = 2; i < 4; i++) {
            for (int j = 5; j > 1; j--) {
                m[j][i].setTile(new Tile(TileType.CAT));
            }
        }
        for (int i = 4; i < 5; i++) {
            for (int j = 5; j > 0; j--) {
                m[j][i].setTile(new Tile(TileType.CAT));
            }
        }
        shelf.setMatrix(m);
        assertEquals(2, shelf.get_max_coloumns());
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
        Tile A1 = new Tile(TileType.CAT);
        Tile A2 = new Tile(TileType.CAT);
        Tile A3 = new Tile(TileType.CAT);
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
        Shelf shelf=new Shelf();
        assertEquals(shelf.N_ROWS, 6);
    }

    @Test
    public void testN_col() {
        Shelf shelf=new Shelf();
        assertEquals(shelf.N_COLS, 5);
    }

}