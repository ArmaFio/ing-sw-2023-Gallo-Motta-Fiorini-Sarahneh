package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.awt.*;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


public class InGameController {

    private ViewGUI gui;
    @FXML
    private GridPane grid;

    public void updateBoard(Tile[][] board) {
        Node result = null;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (!board[i][j].isNone() && !board[i][j].isEmpty()) {
                    result = getNode(grid, j, i);
                    if (result != null) {
                        setTile((ImageView) result, board[i][j]);
                    }
                }
            }
        }
    }

    private Node getNode(GridPane grid, int col, int row) {
        for (Node node : grid.getChildren())
            if (GridPane.getColumnIndex(node) != null
                    && GridPane.getColumnIndex(node) != null
                    && GridPane.getRowIndex(node) == row
                    && GridPane.getColumnIndex(node) == col)
                return node;
        return null;
    }

    private void setTile(ImageView image, Tile tile) {
        Random random = new Random();
        int choice = random.nextInt(3);
        switch (tile.type) {
            case CAT -> {
                switch (choice) {
                    case 0 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Gatti1.1.png")));
                    }
                    case 1 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Gatti1.2.png")));
                    }
                    case 2 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Gatti1.3.png")));
                    }
                }
            }
            case BOOK -> {
                switch (choice) {
                    case 0 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Libri1.1.png")));
                    }
                    case 1 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Libri1.2.png")));
                    }
                    case 2 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Libri1.3.png")));
                    }
                }
            }
            case GAME -> {
                switch (choice) {
                    case 0 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Giochi1.1.png")));
                    }
                    case 1 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Giochi1.2.png")));
                    }
                    case 2 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Giochi1.3.png")));
                    }
                }
            }
            case FRAME -> {
                switch (choice) {
                    case 0 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Cornici1.1.png")));
                    }
                    case 1 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Cornici1.2.png")));
                    }
                    case 2 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Cornici1.3.png")));
                    }
                }
            }
            case PLANT -> {
                switch (choice) {
                    case 0 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Piante1.1.png")));
                    }
                    case 1 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Piante1.2.png")));
                    }
                    case 2 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Piante1.3.png")));
                    }
                }
            }
            case TROPHY -> {
                switch (choice) {
                    case 0 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Trofei1.1.png")));
                    }
                    case 1 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Trofei1.2.png")));
                    }
                    case 2 -> {
                        image.setImage(new Image(getClass().getResourceAsStream("/17_MyShelfie_BGA/item tiles/Trofei1.3.png")));
                    }
                }
            }
        }
    }

    public void setMainApp(ViewGUI gui) {
        this.gui = gui;
    }
}
