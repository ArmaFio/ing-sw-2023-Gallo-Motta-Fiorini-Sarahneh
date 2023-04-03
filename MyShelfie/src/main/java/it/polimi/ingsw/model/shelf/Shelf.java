package it.polimi.ingsw.model.shelf;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.model.TileType;

import java.util.ArrayList;

/**
 * @author Armando Fiorini
 */
public class Shelf {
    public final int N_ROWS = Game.SHELF_ROWS;
    public final int N_COLS = Game.SHELF_COLS;
    private ShelfSlot[][] matrix;

    /**
     * Constructs a Shelf.
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
     * @param nTiles number of tiles the player wants to pick from the board.
     * @return a list containing the indexes of columns which can contain the number of tiles in the argument.
     */
    public ArrayList<Integer> available_columns(int nTiles) {
        int count;
        ArrayList<Integer> l = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            count = 0;
            for (int j = 0, k = 0; j < 6 && k == 0; j++) {
                if (matrix[j][i].getTile().type.isNone())
                    count++;
                else
                    k = 1;
            }
            if (count >= nTiles)
                l.add(i);
        }
        return l;
    }

    /**
     * @return highest number of tiles which can be inserted in the shelf in one turn.
     */
    public int get_max_columns() {
        int max = 0, count;
        for (int i = 0, done = 0; i < 5 && done == 0; i++) {
            count = 0;
            for (int j = 0, k = 0; j < 6 && k == 0; j++) {
                if (matrix[j][i].getTile().type.isNone())
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
    public void putTiles(int col, ArrayList<Tile> tiles) {
        for (int i = 5, j = 0; i >= 0 && j == 0; i--) {
            if (matrix[i][col].getTile().type.isNone()) {
                for (int k = 0; k < tiles.size(); k++) {
                    matrix[i - k][col].setTile(tiles.get(k));


                }
                j = 1;
            }
        }
    }


    /**
     * Get the tile in the given position.
     *
     * @param x row.
     * @param y column.
     * @return tile contained in the [y][x] shelf's ShelfSlot.
     */
    public Tile getTile(int x, int y) {
        return matrix[x][y].getTile();
    }


    /**
     * Assigns a new object to the field 'matrix' of the Shelf.
     *
     * @param matrix new shelf.
     */
    public void setMatrix(ShelfSlot[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * @return content of field matrix of the shelf.
     */
    public ShelfSlot[][] getMatrix() {
        return matrix;
    }

    /**
     * @return An array containing the size of each individual group.
     */
    public int[] find_groups() {
        int[][] groups;
        int[] dimGroups;
        int nGroups = 0;

        groups = new int[this.N_ROWS][this.N_COLS];

        for (int i = 0; i < this.N_ROWS; i++) {
            for (int j = 0; j < this.N_COLS; j++) {
                groups[i][j] = -1;
            }
        }

        for (int i = 0; i < this.N_ROWS; i++) {
            for (int j = 0; j < this.N_COLS; j++) {
                if (groups[i][j] == -1 && !getTile(i, j).type.isNone()) {
                    check_near(groups, nGroups, i, j);
                    nGroups++;
                }
            }
        }

        dimGroups = new int[nGroups];

        for (int i = 0; i < dimGroups.length; i++) {
            dimGroups[i] = 0;
        }

        for (int i = 0; i < this.N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                if (groups[i][j] != -1) {
                    dimGroups[groups[i][j]]++;
                }
            }
        }

        return dimGroups;
    }

    /**
     * Recursive function to find all tiles belonging to the group {@code n} of the {@code Tile} in position ({@code i}, {@code j}).
     * @param m A matrix that where will be added {@code n} to all tiles belonging to the group {@code n}.
     * @param n Index of the group.
     * @param i Position referred to the rows.
     * @param j Position referred to the columns.
     * */
    private void check_near(int[][] m, int n, int i, int j){
        m[i][j] = n;

        if(i + 1 < N_ROWS && getTile(i, j).type.equals(getTile(i + 1, j).type) && m[i + 1][j] == -1) {
            check_near(m, n, i + 1, j);
        }
        if (i - 1 >= 0 && getTile(i, j).type.equals(getTile(i - 1, j).type) && m[i - 1][j] == -1) {
            check_near(m, n, i - 1, j);
        }
        if (j + 1 < N_COLS && getTile(i, j).type.equals(getTile(i, j + 1).type) && m[i][j + 1] == -1) {
            check_near(m, n, i, j + 1);
        }
        if (j - 1 >= 0 && getTile(i, j).type.equals(getTile(i, j - 1).type) && m[i][j - 1] == -1) {
            check_near(m, n, i, j - 1);
        }
    }
}
