package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.tiles.Tile;
import it.polimi.ingsw.server.model.tiles.TileType;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Armando Fiorini
 */
public class Shelf {
    public final String owner;
    public final int N_ROWS = Game.SHELF_ROWS;
    public final int N_COLS = Game.SHELF_COLS;
    private Tile[][] matrix;

    /**
     * Constructs a Shelf.
     */
    public Shelf(String owner) {
        this.owner = owner;
        matrix = new Tile[N_ROWS][N_COLS];
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                matrix[i][j] = new Tile(TileType.EMPTY);
            }
        }
    }

    /**
     * @param nTiles number of tiles the player wants to pick from the board.
     * @return an array containing the indexes of columns which can contain the number of tiles in the argument.
     */
    public int[] availableColumns(int nTiles) {
        int count;
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < N_COLS; i++) {
            count = 0;
            for (int j = 0, k = 0; j < N_ROWS && k == 0; j++) {
                if (matrix[j][i].isEmpty())
                    count++;
                else
                    k = 1;
            }
            if (count >= nTiles)
                list.add(i);
        }

        return list.stream().mapToInt(i -> i).toArray();
    }

    /**
     * @return highest number of tiles which can be inserted in the shelf in one turn.
     */
    public int getMaxColumns() {
        int max = 0, count;
        for (int i = 0, done = 0; i < N_COLS && done == 0; i++) {
            count = 0;
            for (int j = 0, k = 0; j < N_ROWS && k == 0; j++) {
                if (matrix[j][i].isEmpty())
                    count++;
                else
                    k = 1;
            }
            if (count > max)
                max = count;
            if (max >= 3) {
                max = 3;
                done = 1;
            }
        }

        return max;
    }

    /**
     * Puts the assigned tiles in the columns having the assigned index.
     *
     * @param col   column.
     * @param tiles tiles picked from the board.
     */
    public void putTiles(int col, Tile[] tiles) {
        for (int i = N_COLS, j = 0; i >= 0 && j == 0; i--) {
            if (matrix[i][col].isEmpty()) {
                for (int k = 0; k < tiles.length; k++) {
                    matrix[i - k][col] = tiles[k];
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
     * @return tile contained in the [y][x] shelf's slot.
     */
    public Tile getTile(int x, int y) {
        return new Tile(matrix[x][y]);
    }

    /**
     * @return content of field matrix of the shelf.
     */
    public Tile[][] getMatrix() {
        return matrix;
    }

    /**
     * Assigns a new object to the field 'matrix' of the Shelf.
     *
     * @param matrix new shelf.
     */
    public void setMatrix(Tile[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * @return An array containing the size of each individual group.
     */
    public int[] findGroups() {
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
                if (groups[i][j] == -1 && !getTile(i, j).isNone()&&!getTile(i,j).isEmpty()) {
                    checkNear(groups, nGroups, i, j);
                    nGroups++;
                }
            }
        }

        dimGroups = new int[nGroups];

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
     *
     * @param m A matrix that where will be added {@code n} to all tiles belonging to the group {@code n}.
     * @param n Index of the group.
     * @param i Position referred to the rows.
     * @param j Position referred to the columns.
     */
    private void checkNear(int[][] m, int n, int i, int j) {
        m[i][j] = n;

        if (i + 1 < N_ROWS && getTile(i, j).type.equals(getTile(i + 1, j).type) && m[i + 1][j] == -1) {
            checkNear(m, n, i + 1, j);
        }
        if (i - 1 >= 0 && getTile(i, j).type.equals(getTile(i - 1, j).type) && m[i - 1][j] == -1) {
            checkNear(m, n, i - 1, j);
        }
        if (j + 1 < N_COLS && getTile(i, j).type.equals(getTile(i, j + 1).type) && m[i][j + 1] == -1) {
            checkNear(m, n, i, j + 1);
        }
        if (j - 1 >= 0 && getTile(i, j).type.equals(getTile(i, j - 1).type) && m[i][j - 1] == -1) {
            checkNear(m, n, i, j - 1);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(matrix);
    }

    public Tile[][] getShelf() {
        Tile[][] shelf = new Tile[Game.SHELF_ROWS][Game.SHELF_COLS];

        for (int i = 0; i < Game.SHELF_ROWS; i++) {
            System.arraycopy(matrix[i], 0, shelf[i], 0, Game.SHELF_COLS);
        }

        return shelf;
    }
}
