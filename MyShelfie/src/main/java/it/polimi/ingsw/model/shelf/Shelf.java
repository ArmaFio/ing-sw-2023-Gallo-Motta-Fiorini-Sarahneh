package it.polimi.ingsw.model.shelf;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Tile;

import java.util.ArrayList;
import java.util.List;

public class Shelf {
    public final int N_ROWS = Game.SHELF_ROWS;
    public final int N_COLS = Game.SHELF_COLS;
    private ShelfSlot[][] matrix;//TODO aspettiamo Samuele per la classe ShelfSlot

    /**
     * Constructs a Shelf
     */
    public Shelf() {
        matrix = new ShelfSlot[6][5];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = new ShelfSlot();
            }
        }
    }

    /**
     *
     * @param ntiles number of tiles the player wants to pick from the board.
     * @return a list containing the indexes of columns which can contain the number of tiles in the argument.
     */
    public List avaiable_coloumns(int ntiles){
        int count=0;
        List<Integer> l= new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            count = 0;
            for (int j = 0, k = 0; j < 6 && k == 0; j++) {
                if (matrix[j][i].getTile() == null)//TODO aspettiamo Samuele per la classe Tile
                    count++;
                else
                    k = 1;
            }
            if (count >= ntiles)
                l.add(i);
        }
        return l;
    }

    /**
     *
     * @return highest number of tiles which can be inserted in the shelf in one turn
     */
    public int get_max_coloumns(){
        int max=0, count = 0;
        for (int i = 0, done = 0; i < 5 && done == 0; i++) {
            count = 0;
            for (int j = 0, k = 0; j < 6 && k == 0; j++) {
                if (matrix[j][i].getTile() == null)
                    count++;
                else
                    k = 1;
            }
            if (count > max)
                max = count;
            if (max>=3) {
                max = 3;
                done = 1;
            }


        }
        return max;
    }

    /**
     * Puts the assigned tiles in the columns having the assigned index.
     * @param col column.
     * @param tiles tiles picked from the board.
     */
    public void put_tiles (int col, ArrayList<Tile> tiles) {
        for (int i = 5, j = 0; i >= 0 && j == 0; i--) {
            if (matrix[i][col].getTile() == null) {
                for (int k = 0; k < tiles.size(); k++) {
                    matrix[i - k][col].setTile(tiles.get(k));
                }
            }
            j = 1;
        }
    }


    /**
     * Get the tile in the given position.
     * @param x column
     * @param y row
     * @return tile contained in the [y][x] shelf's ShelfSlot.
     */
    public Tile getTile(int x, int y) {
        return matrix[y][x].getTile();
    }


    /**
     * Assigns a new object to the field 'matrix' of the Shelf.
     * @param matrix new shelf.
     */
    public void setMatrix(ShelfSlot[][] matrix) {
        this.matrix = matrix;
    }
}
