/*package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import org.junit.Test;

public class ClientViewTest {


    @Test
    public void shelfWindowTest() {
        int[][] matrix = new int[][]{
                {5, 5, 5, 6, 0},
                {5, 6, 4, 5, 0},
                {3, 3, 3, 0, 1},
                {5, 0, 0, 0, 1},
                {5, 0, 0, 2, 3},
                {5, 3, 3, 2, 5}
        };
        Tile[][] shelf = convert_to_shelf(matrix);
        System.out.print(ClientView.paintWindow(shelf));
    }

    Tile[][] convert_to_shelf(int[][] matrix) {
        Tile[][] shelf = new Tile[ClientView.SHELF_ROWS][ClientView.SHELF_COLS];

        for (int i = 0; i < ClientView.SHELF_ROWS; i++) {
            for (int j = 0; j < ClientView.SHELF_COLS; j++) {
                shelf[i][j] = new Tile(TileType.toEnum(matrix[i][j]));
            }
        }

        return shelf;
    }
}*/