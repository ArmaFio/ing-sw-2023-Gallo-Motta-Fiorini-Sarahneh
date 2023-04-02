package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

public class TilesCombinationTest {
    @Test
    public void combinations() {
        ArrayList<ArrayList<Tile>> result;
        int[][] view = new int[9][9];
        int[][] conf = new int[][]{ //board configuration, 9 represents the taken tiles.
                {0, 0, 0, 9, 9, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 9, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 0, 0, 0}
        };
        Bag bag = new Bag();
        Board board = new Board(4, bag);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (conf[i][j] == 9) {
                    board.getMatrix()[i][j].setTaken(true);
                }
            }
        }
        result = board.getAvailableTiles();
        for (ArrayList<Tile> t : result) {
            System.out.println("Combination with " + t.size() + " tiles.");
            initialize(view);
            for (Tile tile : t) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (board.getMatrix()[i][j].isUsable() && !board.getMatrix()[i][j].occupied() && board.getMatrix()[i][j].getSlotTile().equals(tile)) {
                            view[i][j] = 1;
                        }
                    }
                }
            }
            print(view);
        }
    }

    private void initialize(int[][] matrix) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    private void print(int[][] matrix) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(matrix[i][j] + "  ");
            }
            System.out.print("\n");
        }
    }
}
