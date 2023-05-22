package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.awt.*;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;


public class InGameController {

    private ViewGUI gui;
    @FXML
    private GridPane grid;
    @FXML
    private StackPane shelf1;
    @FXML
    private StackPane shelf2;
    @FXML
    private StackPane shelf3;
    @FXML
    private StackPane shelf4;
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label player3;
    @FXML
    private Label player4;
    private GridPane grid1;
    private GridPane grid2;
    private GridPane grid3;
    private GridPane grid4;


    @FXML
    public void initialize() {
        shelf1.setVisible(false);
        shelf1.setDisable(true);
        shelf2.setVisible(false);
        shelf2.setDisable(true);
        shelf3.setVisible(false);
        shelf3.setDisable(true);
        shelf4.setVisible(false);
        shelf4.setDisable(true);
        player1.setVisible(false);
        player2.setVisible(false);
        player3.setVisible(false);
        player4.setVisible(false);
        grid1 = getGrid(shelf1);
        grid2 = getGrid(shelf2);
        grid3 = getGrid(shelf3);
        grid4 = getGrid(shelf4);
    }


    /**
     * Gets the grid associated to the specified shelf.
     *
     * @param shelf the shelf you want to get the grid of.
     * @return shelf's grid.
     */
    private GridPane getGrid(StackPane shelf) {
        GridPane result = null;
        for (Node node : shelf.getChildren()) {
            if (node instanceof GridPane) {
                result = (GridPane) node;
            }
        }
        return result;
    }

    @FXML
    private void mouseEntered(MouseEvent e) {
        Node source = (Node) e.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        if (colIndex != null && rowIndex != null) {
            //System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
            Node node = getNode(grid, colIndex, rowIndex);
            node.setEffect(new Glow(0.8));
        }
    }

    @FXML
    private void mouseExited(MouseEvent e) {
        Node source = (Node) e.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        if (colIndex != null && rowIndex != null) {
            //System.out.printf("Mouse exited cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
            Node node = getNode(grid, colIndex, rowIndex);
            node.setEffect(null);
        }
    }

    public void updateBoard(Tile[][] board) {
        Node result = null;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
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
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
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
                        //image.setEffect(new Glow(0.8)); //TODO aggiungere questo quando si passa il cursore
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

    public void enableShelves(int length) {
        switch (length) {
            case 2 -> {
                shelf1.setVisible(true);
                shelf1.setDisable(false);
                shelf2.setVisible(true);
                shelf2.setDisable(false);
            }
            case 3 -> {
                shelf1.setVisible(true);
                shelf1.setDisable(false);
                shelf2.setVisible(true);
                shelf2.setDisable(false);
                shelf3.setVisible(true);
                shelf3.setDisable(false);
            }
            case 4 -> {
                shelf1.setVisible(true);
                shelf1.setDisable(false);
                shelf2.setVisible(true);
                shelf2.setDisable(false);
                shelf3.setVisible(true);
                shelf3.setDisable(false);
                shelf4.setVisible(true);
                shelf4.setDisable(false);
            }
        }
    }
}
