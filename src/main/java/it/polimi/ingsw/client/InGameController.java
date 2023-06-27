package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.ChatMessage;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.messages.TilesResponse;
import it.polimi.ingsw.server.model.Tile;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.utils.Logger;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;


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
    @FXML
    private Button done;
    @FXML
    private ImageView personal;
    @FXML
    private ImageView common1;
    @FXML
    private ImageView common2;
    @FXML
    private ImageView commonScore1;
    @FXML
    private ImageView commonScore2;
    @FXML
    private Label endMessage;
    @FXML
    private TextField chatBar;
    @FXML
    private VBox chatBox;
    private GridPane grid1;
    private GridPane grid2;
    private GridPane grid3;
    private GridPane grid4;
    private final LinkedHashMap<Label, StackPane> shelvesName = new LinkedHashMap<>();
    private boolean firstTile;
    private int numTiles;
    private ArrayList<Tile> tilesInserted;
    private int selectedCol;
    private Tile[][] firstBoard;
    private Tile[][] board;
    private boolean firstBoardUpdate;
    private List<List<Tile>> combList;
    private boolean firstComb;
    private final ImageView[] commons = new ImageView[2];
    private final HashMap<Integer, ImageView> commonsAndId = new HashMap<>();
    private int chatShift;


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
        done.setDisable(true);
        done.setVisible(false);
        endMessage.setVisible(false);
        firstTile = true;
        tilesInserted = new ArrayList<>();
        numTiles = 0;
        selectedCol = -1;
        firstBoardUpdate = true;
        combList = new ArrayList<>();
        firstComb = true;
        commons[0] = common1;
        commons[1] = common2;
        chatBox.setStyle("-fx-background-color:#333333;");
        chatShift = 0;
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

    /**
     * @param shelf the shelf of the player we want to add points to.
     * @return an {@code Array} of {@code ImageView} where score tokens can be put.
     */
    private ImageView[] getPointsSlots(StackPane shelf) {
        ImageView[] result = new ImageView[2];
        List<ImageView> slots = new ArrayList<>();
        for (Node node : shelf.getChildren()) {
            if (node instanceof ImageView) {
                slots.add((ImageView) node);
            }
        }
        result = slots.toArray(result);
        return result;
    }

    /**
     * Function used to show on screen the image related to the {@code PersonalGoal}'s id.
     *
     * @param personalId the id of the personal goal.
     */
    public void setPersonal(int personalId) {
        switch (personalId) {
            case 0 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals.png"))));
            }
            case 1 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals12.png"))));
            }
            case 2 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals11.png"))));
            }
            case 3 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals10.png"))));
            }
            case 4 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals9.png"))));
            }
            case 5 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals8.png"))));
            }
            case 6 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals7.png"))));
            }
            case 7 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals6.png"))));
            }
            case 8 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals5.png"))));
            }
            case 9 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals4.png"))));
            }
            case 10 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals3.png"))));
            }
            case 11 -> {
                personal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/personal goal cards/Personal_Goals2.png"))));
            }
        }
    }

    /**
     * Displays the images associated to the {@code CommonGoalCards}.
     *
     * @param goals the array containing the common goal cards.
     */
    public void setCommonGoals(CommonGoalCard[] goals) {
        for (int i = 0; i < 2; i++) {
            switch (goals[i].id) {
                case 1 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/4.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 2 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/3.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 3 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/8.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 4 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/1.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 5 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/5.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 6 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/9.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 7 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/11.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 8 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/7.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 9 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/2.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 10 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/6.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 11 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/10.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
                case 12 -> {
                    commons[i].setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/common goal cards/12.jpg"))));
                    if (i == 0) {
                        commonsAndId.put(goals[i].id, commonScore1);
                    } else {
                        commonsAndId.put(goals[i].id, commonScore2);
                    }
                }
            }
        }

    }

    /**
     * Method used to set a glow effect on the tile pointed by the mouse.
     *
     * @param e the {@code MouseEvent}.
     */
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


    /**
     * Removes the glow from the tile previously pointed by the mouse.
     *
     * @param e the {@code MouseEvent}.
     */
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

    /**
     * Method called when a drag action is detected.
     *
     * @param e the {@code MouseEvent}.
     */
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


    /**
     * Sets the background for the shelf's column in which the mouse is pointing while drag action.
     *
     * @param e the {@code MouseEvent}.
     */
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

    /**
     * Resets the background for the shelf's column in which the mouse was pointing while in drag action.
     *
     * @param e the {@code MouseEvent}.
     */
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

    /**
     * Places the dragged tile in the selected column of the shelf.
     *
     * @param e the {@code MouseEvent}.
     */
    @FXML
    private void onDragDropped(DragEvent e) {
        onDragExited(e);
        Node node = (Node) e.getTarget();
        Dragboard db = e.getDragboard();
        if (db.hasImage()) {
            boolean success = false;
            Integer colIndex = GridPane.getColumnIndex(node);
            if (firstTile) {
                selectedCol = colIndex;
                int colNum = ((GridPane) node.getParent()).getColumnCount();
                for (int i = 0; i < colNum; i++) {
                    if (i != colIndex) {
                        ArrayList<Node> colNodes = getNode((GridPane) node.getParent(), i);
                        for (Node n : colNodes) {
                            n.setDisable(true);
                        }
                    }
                }
                firstTile = false;
            }
            if (tilesInserted.size() < 3) {
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
                    numTiles++;
                    Node source = (Node) e.getGestureSource();
                    Integer col = GridPane.getColumnIndex(source);
                    Integer row = GridPane.getRowIndex(source);
                    tilesInserted.add(gui.getBoard()[row][col]);
                    e.setDropCompleted(success);
                    e.consume();
                }
            } else {
                Logger.debug("Tiles limit reached");
            }
        }
    }

    /**
     * Finalizes the drag and drop action.
     *
     * @param e the {@code MouseEvent}.
     */
    @FXML
    private void onDragDone(DragEvent e) {
        if (e.getTransferMode() == TransferMode.MOVE) {
            Node source = (Node) e.getSource();
            source.setVisible(false);
            //((ImageView) source).setImage(null);
            System.gc();
            e.consume();
            done.setVisible(true);
            done.setDisable(false);
            updateTiles(source);
        }
    }

    /**
     * Function called when the player ends his turn by clicking the button done.
     */
    @FXML
    private void onDone() {
        firstTile = true;
        done.setDisable(true);
        done.setVisible(false);
        GridPane shelf;
        for (Label label : shelvesName.keySet()) {
            if (label.getText().equals(gui.getCurrentPlayer())) {
                shelf = getGrid(shelvesName.get(label));
                for (int i = 0; i < shelf.getRowCount(); i++) {
                    for (int j = 0; j < shelf.getColumnCount(); j++) {
                        Node node = getNode(shelf, j, i);
                        node.setDisable(false);
                    }
                }
            }
        }
        TilesResponse response = new TilesResponse(tilesInserted.toArray(new Tile[0]));
        try {
            gui.write(response);
        } catch (IOException e) {
            Logger.error("Unable to send tiles response");
        }
        tilesInserted.clear();
        combList = new ArrayList<>();
        firstComb = true;
        //disable all the board
        for (int i = 0; i < grid.getRowCount(); i++) {
            for (int j = 0; j < grid.getColumnCount(); j++) {
                Node target = getNode(grid, j, i);
                if (target != null) {
                    target.setDisable(true);
                }
            }
        }

    }

    /**
     * Function called after pressing {@code Enter} in the chatBox to send the message.
     */
    @FXML
    private void writeOnChat() {
        if (!chatBar.getText().isBlank()) {
            Label message = new Label("[" + gui.getUsername() + "] " + chatBar.getText());
            message.setAlignment(Pos.CENTER_RIGHT);
            chatBox.getChildren().add(message);
            StringMessage response = new StringMessage(chatBar.getText());
            try {
                gui.write(response);
            } catch (IOException e) {
                Logger.error("Unable to send chat message");
            }
            chatBar.clear();
        } else {
            chatBar.clear();
        }
    }

    /**
     * @return the {@code selectedColumn} for this turn.
     */
    public int getSelectedCol() {
        return selectedCol;
    }

    /**
     * Updates the state of the board.
     *
     * @param board the current state of the board.
     */
    public void updateBoard(Tile[][] board) {
        boolean refill = false;
        this.board = board;
        if (firstBoardUpdate) {
            firstBoard = board;
            firstBoardUpdate = false;
        }
        Node result = null;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (!board[i][j].isNone() && !board[i][j].isEmpty()) {
                    result = getNode(grid, j, i);
                    if (result != null && ((ImageView) result).getImage() == null) {
                        setTile((ImageView) result, board[i][j]);
                    } else if (result != null && ((ImageView) result).getImage() != null && !result.isVisible()) {
                        setTile((ImageView) result, board[i][j]);
                        result.setVisible(true);
                        refill = true;
                    }
                } else if (board[i][j].isEmpty()) {
                    result = getNode(grid, j, i);
                    result.setVisible(false);
                    //((ImageView) result).setImage(null);
                }
            }
        }
        if (refill) {
            firstBoard = board;
            setActiveTiles();
        }
        System.gc();
    }

    /**
     * @param source the node putted in the shelf
     */
    private void updateTiles(Node source) {
        //disable all the board
        for (int i = 0; i < grid.getRowCount(); i++) {
            for (int j = 0; j < grid.getColumnCount(); j++) {
                Node target = getNode(grid, j, i);
                if (target != null) {
                    target.setDisable(true);
                }
            }
        }

        //convert to list
        if (firstComb) {
            for (Tile[] comb : gui.getAvailableTiles()) {
                if (comb.length > 1) {
                    combList.add(Arrays.asList(comb));
                }
            }
            firstComb = false;
        }

        //filter list
        Integer col = GridPane.getColumnIndex(source);
        Integer row = GridPane.getRowIndex(source);
        List<List<Tile>> result = new ArrayList<>();
        for (List<Tile> list : combList) {
            boolean found = false;
            if (list.size() == tilesInserted.size() + 1) {
                for (Tile t : list) {
                    if (t.equalsId(firstBoard[row][col]))
                        found = true;
                }
                if (found)
                    result.add(list);
            }
        }
        firstComb = true;
        combList = new ArrayList<>();
        combList.addAll(result);

        //enable only allowed tiles
        for (List<Tile> list : combList) {
            for (Tile t : list) {
                for (int i = 0; i < firstBoard.length; i++) {
                    for (int j = 0; j < firstBoard[0].length; j++) {
                        if (firstBoard[i][j].id == t.id) {
                            getNode(grid, j, i).setDisable(false);
                        }
                    }
                }
            }
        }

    }

    /**
     * Gives a visual feedback while completing a {@code commonGoal} assigning the tile to the player.
     *
     * @param points   the amount of points earned by the player for completing the common goal.
     * @param commonId the common goal completed by the player.
     * @param player   the player who completed the common goal.
     */
    public void updatePoints(int points, int commonId, String player) {
        switch (points) {
            case 2 -> {
                ImageView target = commonsAndId.get(commonId);
                target.setImage(null);
                addScoreImage(player, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_2.jpg"))));
                System.gc();
            }
            case 4 -> {
                if (shelvesName.size() == 2 || shelvesName.size() == 3) {
                    ImageView target = commonsAndId.get(commonId);
                    target.setImage(null);
                    addScoreImage(player, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_4.jpg"))));
                    System.gc();
                } else {
                    ImageView target = commonsAndId.get(commonId);
                    target.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_2.jpg"))));
                    addScoreImage(player, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_4.jpg"))));
                    System.gc();
                }
            }
            case 6 -> {
                ImageView target = commonsAndId.get(commonId);
                target.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_4.jpg"))));
                addScoreImage(player, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_6.jpg"))));
                System.gc();
            }
            case 8 -> {
                if (shelvesName.size() == 2) {
                    ImageView target = commonsAndId.get(commonId);
                    target.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_4.jpg"))));
                    addScoreImage(player, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_8.jpg"))));
                    System.gc();
                } else {
                    ImageView target = commonsAndId.get(commonId);
                    target.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_6.jpg"))));
                    addScoreImage(player, new Image(Objects.requireNonNull(getClass().getResourceAsStream("/17_MyShelfie_BGA/scoring tokens/scoring_8.jpg"))));
                    System.gc();
                }
            }

        }
    }

    /**
     * Sets a visual feedback for the player who completed the common goal.
     *
     * @param player the player who completed the common goal.
     * @param image  the image that must be shown near the {@code player}'s shelf.
     */
    private void addScoreImage(String player, Image image) {
        for (Label label : shelvesName.keySet()) {
            if (label.getText().equals(player)) {
                StackPane target = shelvesName.get(label);
                ImageView[] slots = getPointsSlots(target);
                for (ImageView slot : slots) {
                    if (slot.getImage() == null) {
                        slot.setImage(image);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Enables only the tiles that can be picked on the board at the beginning of the turn.
     */
    public void setActiveTiles() {
        //disable all the board at the beginning
        for (int i = 0; i < grid.getRowCount(); i++) {
            for (int j = 0; j < grid.getColumnCount(); j++) {
                Node target = getNode(grid, j, i);
                if (target != null) {
                    target.setDisable(true);
                }
            }
        }
        //enables only the allowed tiles
        for (Tile[] tile : gui.getAvailableTiles()) {
            boolean found = false;
            if (tile.length == 1) {
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[0].length; j++) {
                        if (board[i][j].id == tile[0].id) {
                            Node target = getNode(grid, j, i);
                            target.setDisable(false);
                            found = true;
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    /**
     * Updates the tiles in the shelves.
     *
     * @param shelves all the player's shelves.
     */
    public void updateShelves(HashMap<String, Tile[][]> shelves) {
        for (Label label : shelvesName.keySet()) {
            GridPane grid = (GridPane) shelvesName.get(label).getChildren().get(1);
            for (String name : shelves.keySet()) {
                if (name.equals(label.getText())) {
                    for (int i = 0; i < shelves.get(name).length; i++) {
                        for (int j = 0; j < shelves.get(name)[0].length; j++) {
                            if (shelves.get(name)[i][j].type != TileType.EMPTY) {
                                ImageView target = ((ImageView) getNode(grid, j, i));
                                if (target.getImage() == null) {
                                    for (int h = 0; h < firstBoard.length; h++) {
                                        for (int k = 0; k < firstBoard[0].length; k++) {
                                            if (firstBoard[h][k].id == shelves.get(name)[i][j].id) {
                                                Image image = (((ImageView) getNode(this.grid, k, h)).getImage());
                                                target.setImage(image);
                                            }
                                        }
                                    }
                                    if (target.getImage() == null) {
                                        setTile(target, shelves.get(name)[i][j]);
                                    }
                                    //target.setImage(((ImageView)getNode(this.grid, j, i)).getImage());
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    /**
     * Sets the current player and applies a visual indicator in the gui.
     *
     * @param currentPlayer the current player.
     */
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

    /**
     * Gets the node in the {@code GridPane} given the coordinates.
     *
     * @param grid the grid containing the node we are looking for.
     * @param col  the column of the node of interest.
     * @param row  the row of the node of interest.
     * @return the node in the specified location.
     */
    private Node getNode(GridPane grid, int col, int row) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * @param grid the grid containing the nodes we are looking for.
     * @param col  the column of interest.
     * @return all the nodes in the specified column.
     */
    private ArrayList<Node> getNode(GridPane grid, int col) {
        ArrayList<Node> result = new ArrayList<>();
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col) {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * sets the tile image in the view to match the tile type in the board.
     *
     * @param image the tile in the view.
     * @param tile  the tile in the board.
     */
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

    /**
     * Sets the reference for the view.
     *
     * @param gui the view.
     */
    public void setMainApp(ViewGUI gui) {
        this.gui = gui;
    }

    /**
     * Displays shelves based on the number of players.
     *
     * @param length the number of players.
     */
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

    /**
     * handles the end game message.
     *
     * @param message the message received from the server.
     */
    public void onStringRequest(StringMessage message) {
        endMessage.setText(message.message());
        endMessage.setVisible(true);
        System.gc();
    }

    /**
     * Function used to update the chat every time a new message is received.
     *
     * @param chat the history of chat messages in chronological order.
     */
    public void updateChat(ChatMessage[] chat) {
        chatBox.getChildren().clear();
        for (int i = chatShift; i < chat.length; i++) {
            Label message = new Label("[" + chat[i].getAuthor() + "] " + chat[i].getMessage());
            message.setMaxWidth(chatBox.getMaxWidth());
            message.setStyle("-fx-background-color:purple;" +
                    "  -fx-text-fill:white;" +
                    " -fx-pref-height:20px;" +
                    "  -fx-pref-width:221px; ");
            if (chat[i].getAuthor().equals(gui.getUsername())) {
                message.setAlignment(Pos.CENTER_RIGHT);
            } else {
                message.setAlignment(Pos.CENTER_LEFT);
            }
            chatBox.getChildren().add(message);
        }
    }

    public void setChatShift(int shift) {
        chatShift = shift;
    }
}
