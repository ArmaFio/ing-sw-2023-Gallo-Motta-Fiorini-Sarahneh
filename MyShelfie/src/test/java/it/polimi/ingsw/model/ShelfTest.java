package it.polimi.ingsw.model;

import it.polimi.ingsw.model.shelf.Shelf;
import junit.framework.TestCase;

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
        Slot[][] m = new Slot[5][6];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                m[i][j]= new Slot();
                m[i][j].tile = new Tile();
            }
        }
        shelf.setMatrix(m);
        assertTrue(shelf.avaiable_coloumns(3).size() == 0);
        assertTrue(shelf.avaiable_coloumns(2).size() == 0);
        assertTrue(shelf.avaiable_coloumns(1).size() == 0);
    }

    public void testGet_max_coloumns() {
    }

    public void testPut_tiles() {
    }

    public void testGet_tile() {
    }

    public void testN_row() {
        Shelf shelf=new Shelf();
        assertEquals(shelf.n_row(),5);
    }

    public void testN_col() {
        Shelf shelf=new Shelf();
        assertEquals(shelf.n_col(),6);
    }

    public void testSetMatrix() {
    }
}