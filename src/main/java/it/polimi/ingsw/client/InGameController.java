package it.polimi.ingsw.client;

import it.polimi.ingsw.server.model.Tile;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
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
    private ArrayList<Label> players = new ArrayList<>();
    private GridPane grid1;
    private GridPane grid2;
    private GridPane grid3;
    private GridPane grid4;
    private LinkedHashMap<Label, StackPane> shelvesName = new LinkedHashMap<>();


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

    @FXML
    private void onDragDetected(MouseEvent e) {
        //TODO put here drag condition
        Node source = (Node) e.getSource();
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        if (colIndex != null && rowIndex != null) {
            //System.out.printf("Mouse entered cell [%d, %d]%n", colIndex.intValue(), rowIndex.intValue());
            Node node = getNode(grid, colIndex, rowIndex);
            if (((ImageView) node).getImage() != null) {
                Dragboard db = node.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                ImageView im = (ImageView) node;
                content.putImage(im.getImage());
                db.setContent(content);
            }
        }
    }

    @FXML
    private void onDragOver(DragEvent e) {
        e.acceptTransferModes(TransferMode.MOVE);
        e.consume();
    }

    @FXML
    private void onDragEntered(DragEvent e) {
        Node node = (Node) e.getTarget();
        Integer colIndex = GridPane.getColumnIndex(node);
        ArrayList<Node> colNodes = getNode((GridPane) node.getParent(), colIndex);
        for (Node n : colNodes) {
            ImageView image = (ImageView) n;
            if (image.getImage() == null) {
                image.setPreserveRatio(false);
                image.setImage(new Image(getClass().getResourceAsStream("/images/green background.png")));
            }
        }
        e.consume();
    }

    @FXML
    private void onDragExited(DragEvent e) {
        Node node = (Node) e.getTarget();
        Integer colIndex = GridPane.getColumnIndex(node);
        ArrayList<Node> colNodes = getNode((GridPane) node.getParent(), colIndex);
        for (Node n : colNodes) {
            if (((ImageView) n).getImage() != null && !((ImageView) n).isPreserveRatio()) {
                //n.setCache(false);
                ((ImageView) n).setPreserveRatio(true);
                ((ImageView) n).setImage(null);
                System.gc();
            }
        }
        e.consume();
    }

    @FXML
    private void onDragDropped(DragEvent e) {
        onDragExited(e);
        Node node = (Node) e.getTarget();
        Dragboard db = e.getDragboard();
        if (db.hasImage()) {
            boolean success = false;
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer maxRow = -1;
            ArrayList<Node> colNodes = getNode((GridPane) node.getParent(), colIndex);
            GridPane grid = (GridPane) node.getParent();
            for (Node n : colNodes) {
                if (((ImageView) n).getImage() == null && GridPane.getRowIndex(n) > maxRow) {
                    maxRow = GridPane.getRowIndex(n);
                }
            }
            if (maxRow >= 0) {
                success = true;
                Node target = getNode(grid, colIndex, maxRow);
                ((ImageView) target).setPreserveRatio(true);
                ((ImageView) target).setImage(db.getImage());
                e.setDropCompleted(success);
                e.consume();
            }
        }
    }

    @FXML
    private void onDragDone(DragEvent e) {
        if (e.getTransferMode() == TransferMode.MOVE) {
            Node source = (Node) e.getSource();
            ((ImageView) source).setImage(null);
            System.gc();
            e.consume();
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

    public void setCurrentPlayer(String currentPlayer) {
        for (Label p : shelvesName.keySet()) {
            if (p.getText().equals(currentPlayer)) {
                p.setTextFill(Color.GREEN);
                if (p.getText().equals(gui.getUsername())) {
                    shelvesName.get(p).setDisable(false);
                } else {
                    shelvesName.get(p).setDisable(true);
                }
            } else {
                p.setTextFill(Color.BLACK);
                shelvesName.get(p).setDisable(true); //TODO should not allow the player to put tiles in other's player shelves
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

    private ArrayList<Node> getNode(GridPane grid, int col) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col) {
                result.add(node);
            }
        }
        return result;
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
                shelvesName.put(player1, shelf1);
                shelvesName.put(player2, shelf2);

                for (Label p : shelvesName.keySet()) {
                    shelvesName.get(p).setVisible(true);
                    //shelvesName.get(p).setDisable(false);
                }

                int i = 0;
                String[] users = gui.getLobbyUsers();
                for (Label p : shelvesName.keySet()) {
                    p.setVisible(true);
                    p.setText(users[i]);
                    i++;
                }
            }
            case 3 -> {
                shelvesName.put(player1, shelf1);
                shelvesName.put(player2, shelf2);
                shelvesName.put(player3, shelf3);

                for (Label p : shelvesName.keySet()) {
                    shelvesName.get(p).setVisible(true);
                    //shelvesName.get(p).setDisable(false);
                }

                int i = 0;
                String[] users = gui.getLobbyUsers();
                for (Label p : shelvesName.keySet()) {
                    p.setVisible(true);
                    p.setText(users[i]);
                    i++;
                }
            }
            case 4 -> {
                shelvesName.put(player1, shelf1);
                shelvesName.put(player2, shelf2);
                shelvesName.put(player3, shelf3);
                shelvesName.put(player4, shelf4);

                for (Label p : shelvesName.keySet()) {
                    shelvesName.get(p).setVisible(true);
                    //shelvesName.get(p).setDisable(false);
                }

                int i = 0;
                String[] users = gui.getLobbyUsers();
                for (Label p : shelvesName.keySet()) {
                    p.setVisible(true);
                    p.setText(users[i]);
                    i++;
                }
            }
        }
    }
}
