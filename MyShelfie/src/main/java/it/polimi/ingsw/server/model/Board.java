package it.polimi.ingsw.server.model;

import it.polimi.ingsw.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Board {

    private final Tile[][] matrix;
    private final int nPlayers; //number of players for this board
    private final Bag bag;  //bag used for this game
    private int nTiles; //number of tiles currently on the board

    /**
     * Constructor: initialize the board assigning tiles picked from bag to slots.
     *
     * @param nPlayers number of players.
     * @param bag      bag from which tiles are going to be drawn for the entire game.
     */
    public Board(int nPlayers, Bag bag) { //TODO la draw falla in un metodo che chiami quando ti serve
        nTiles = 0;
        this.bag = bag;
        this.nPlayers = nPlayers;
        matrix = new Tile[Game.BOARD_DIM][Game.BOARD_DIM];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (Game.boardConfiguration[i][j] == 0) {
                    matrix[i][j] = new Tile(TileType.NONE);
                } else {
                    if (this.nPlayers >= Game.boardConfiguration[i][j]) {
                        matrix[i][j] = bag.draw();
                        matrix[i][j].setpos(i, (char) ('A' + j));
                        nTiles++;
                    } else {
                        matrix[i][j] = new Tile(TileType.NONE);
                        nTiles++;
                    }
                }
            }
        }
    }

    /**
     * Get the reference of the tile from the board.
     *
     * @param x row of the board.
     * @param y column of the board.
     * @return reference to the tile located in matrix[x][y].
     */
    public Tile getTile(int x, int y) {
        return matrix[y][x];
    }

    /**
     * @return all the possible combinations of tiles that can be picked in this turn.
     */
    public Tile[][] getAvailableTiles() { //TODO fix, returns wrong combinations
        ArrayList<ArrayList<Tile>> result = new ArrayList<>();
        //get all possible tiles as first pick;
        for (Tile t : getSomeAvailableTiles()) {
            ArrayList<Tile> temp = new ArrayList<>();
            temp.add(t);
            result.add(temp);
        }
        //get all two tiles combinations.
        ArrayList<ArrayList<Tile>> toAdd = new ArrayList<>();
        for (ArrayList<Tile> t : result) {
            for (Tile t1 : getSomeAvailableTiles(t.get(0))) {
                ArrayList<Tile> temp = new ArrayList<>();
                temp.add(t.get(0));
                temp.add(t1);
                //checks for duplicates
                int cont = 0;
                for (ArrayList<Tile> r : toAdd) {
                    if (new HashSet<>(r).equals(new HashSet<>(temp))) {
                        cont++;
                    }
                }
                System.out.println(" ");
                if (cont == 0) {
                    toAdd.add(temp);
                }
            }
        }
        result.addAll(toAdd);
        //get all three tiles combinations.
        ArrayList<ArrayList<Tile>> toAdd3 = new ArrayList<>();
        for (ArrayList<Tile> t : result) {
            if (t.size() > 1) {
                for (Tile t3 : getSomeAvailableTiles(t.get(0), t.get(1))) {
                    ArrayList<Tile> temp = new ArrayList<>();
                    temp.add(t.get(0));
                    temp.add(t.get(1));
                    temp.add(t3);
                    int cont = 0;
                    for (ArrayList<Tile> f : toAdd3) {
                        if (new HashSet<>(f).equals(new HashSet<>(temp))) {
                            cont++;
                        }
                    }
                    System.out.println(" ");
                    if (cont == 0) {
                        toAdd3.add(temp);
                    }
                }
            }
        }

        result.addAll(toAdd3);

        return result.stream().map(e -> e.toArray(new Tile[0])).toArray(Tile[][]::new);
    }

    /**
     * Getter method.
     *
     * @return ArrayList of tiles that can be picked up as first tile at the beginning of player's turn.
     */
    public ArrayList<Tile> getSomeAvailableTiles() { // TODO Deve restituire tutte le combinazioni di tessere
        ArrayList<Tile> availableTiles = new ArrayList<>();
        //look for available tiles on the board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!matrix[i][j].isNone() && !matrix[i][j].isEmpty()) {
                    if (hasFreeSide(i, j)) {
                        availableTiles.add(matrix[i][j]);
                    }
                }
            }
        }
        return availableTiles;
    }

    /**
     * Called by the game logic if the player decides to pick a second tile.
     *
     * @param tile first tile chosen by the player in this turn.
     * @return all the possible tiles you can pick as second tile in this turn.
     */
    public ArrayList<Tile> getSomeAvailableTiles(Tile tile) {
        ArrayList<Tile> availableTiles = new ArrayList<>();
        int[] index = getTileIndex(tile);
        if (index[1] - 1 >= 0 && !matrix[index[1] - 1][index[0]].isNone() && !matrix[index[1] - 1][index[0]].isEmpty() && hasFreeSide(index[1] - 1, index[0])) {
            availableTiles.add(matrix[index[1] - 1][index[0]]);
        }

        if (index[1] + 1 <= 8 && !matrix[index[1] + 1][index[0]].isNone() && !matrix[index[1] + 1][index[0]].isEmpty() && hasFreeSide(index[1] + 1, index[0])) {
            availableTiles.add(matrix[index[1] + 1][index[0]]);
        }

        if (index[0] - 1 >= 0 && !matrix[index[1]][index[0] - 1].isNone() && !matrix[index[1]][index[0] - 1].isEmpty() && hasFreeSide(index[1], index[0] - 1)) {
            availableTiles.add(matrix[index[1]][index[0] - 1]);
        }

        if (index[0] + 1 <= 8 && !matrix[index[1]][index[0] + 1].isNone() && !matrix[index[1]][index[0] + 1].isEmpty() && hasFreeSide(index[1], index[0] + 1)) {
            availableTiles.add(matrix[index[1]][index[0] + 1]);
        }

        return availableTiles;
    }

    /**
     * Called by the game logic if the player decides to pick a third tile.
     *
     * @param t1 picked tile.
     * @param t2 picked tile.
     * @return all the possible tiles you can pick as third tile in this turn.
     */
    public ArrayList<Tile> getSomeAvailableTiles(Tile t1, Tile t2) {
        ArrayList<Tile> availableTiles = new ArrayList<Tile>();
        int[] index1 = getTileIndex(t1);
        int[] index2 = getTileIndex(t2);
        if (index1[0] == index2[0]) { //same column
            switch (index1[1] - index2[1]) {
                case -1 -> {  //t1 above t2
                    if (index1[1] - 1 >= 0 && !matrix[index1[1] - 1][index1[0]].isNone() && !matrix[index1[1] - 1][index1[0]].isEmpty() && hasFreeSide(index1[1] - 1, index1[0])) {
                        availableTiles.add(matrix[index1[1] - 1][index1[0]]);
                    }
                    if (index2[1] + 1 <= 8 && !matrix[index2[1] + 1][index2[0]].isNone() && !matrix[index2[1] + 1][index2[0]].isEmpty() && hasFreeSide(index2[1] + 1, index2[0])) {
                        availableTiles.add(matrix[index2[1] + 1][index2[0]]);
                    }
                }
                case 1 -> { //t2 above t1
                    if (index2[1] - 1 >= 0 && !matrix[index2[1] - 1][index2[0]].isNone() && !matrix[index2[1] - 1][index2[0]].isEmpty() && hasFreeSide(index2[1] - 1, index2[0])) {
                        availableTiles.add(matrix[index2[1] - 1][index2[0]]);
                    }
                    if (index1[1] + 1 <= 8 && !matrix[index1[1] + 1][index1[0]].isNone() && !matrix[index1[1] + 1][index1[0]].isEmpty() && hasFreeSide(index1[1] + 1, index1[0])) {
                        availableTiles.add(matrix[index1[1] + 1][index1[0]]);
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + (index1[1] - index2[1]));
            }
            return availableTiles;
        }

        if (index1[1] == index2[1]) { //same row
            switch (index1[0] - index2[0]) {
                case 1 -> { //t2 before t1
                    if (index1[0] + 1 <= 8 && !matrix[index1[1]][index1[0] + 1].isNone() && !matrix[index1[1]][index1[0] + 1].isEmpty() && hasFreeSide(index1[1], index1[0] + 1)) {
                        availableTiles.add(matrix[index1[1]][index1[0] + 1]);
                    }
                    if (index2[0] - 1 >= 0 && !matrix[index2[1]][index2[0] - 1].isNone() && !matrix[index2[1]][index2[0] - 1].isEmpty() && hasFreeSide(index2[1], index2[0] - 1)) {
                        availableTiles.add(matrix[index2[1]][index2[0] - 1]);
                    }
                }
                case -1 -> { //t1 before t2
                    if (index1[0] - 1 >= 0 && !matrix[index1[1]][index1[0] - 1].isNone() && !matrix[index1[1]][index1[0] - 1].isEmpty() && hasFreeSide(index1[1], index1[0] - 1)) {
                        availableTiles.add(matrix[index1[1]][index1[0] - 1]);
                    }
                    if (index2[0] + 1 <= 8 && !matrix[index2[1]][index2[0] + 1].isNone() && !matrix[index2[1]][index2[0] + 1].isEmpty() && hasFreeSide(index2[1], index2[0] + 1)) {
                        availableTiles.add(matrix[index2[1]][index2[0] + 1]);
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + (index1[0] - index2[0]));
            }
            return availableTiles;
        }


        return null;
    }

    /**
     * Get the index of a specified tile.
     *
     * @param tile the tile I want to know the position in the board.
     * @return an index[2] containing the column and the row (in this order).
     */
    private int[] getTileIndex(Tile tile) {
        int[] index = new int[2];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (matrix[i][j].equalsId(tile)) {  //equals has a new definition in tile
                    index[0] = j;  //x
                    index[1] = i;  //y
                }
            }
        }
        return index;
    }


    /**
     * Verify if the tile in the [y][x] position has at least one free side.
     *
     * @param y row.
     * @param x column.
     * @return true if the tile has a free side, false otherwise.
     */
    public boolean hasFreeSide(int y, int x) {  //made public for test purposes, valuate to set as private after testing
        if (y - 1 < 0 || y + 1 > 8 || x - 1 < 0 || x + 1 > 8) {  //check edge position cases
            return true;
        }
        if (matrix[y + 1][x].isNone() || !matrix[y + 1][x].isNone() && matrix[y + 1][x].isEmpty()) {
            return true;
        }

        if (matrix[y - 1][x].isNone() || !matrix[y - 1][x].isNone() && matrix[y - 1][x].isEmpty()) {
            return true;
        }

        if (matrix[y][x + 1].isNone() || !matrix[y][x + 1].isNone() && matrix[y][x + 1].isEmpty()) {
            return true;
        }

        return matrix[y][x - 1].isNone() || !matrix[y][x - 1].isNone() && matrix[y][x - 1].isEmpty();
    }

    /**
     * Check if the tile in the given position has all its four adjacent slots free on the board.
     *
     * @param y row.
     * @param x column.
     * @return true if the tiles has all the four sides free, false otherwise.
     */
    public boolean hasAllFreeSides(int y, int x) {  //made public for test purposes, valuate to set as private after testing
        int count = 0;
        if (y - 1 < 0) {  //check edge position cases
            count++;
        }
        if (y + 1 > 8) {  //check edge position cases
            count++;
        }
        if (x - 1 < 0) {  //check edge position cases
            count++;
        }
        if (x + 1 > 8) {  //check edge position cases
            count++;
        }
        if (y + 1 <= 8 && (matrix[y + 1][x].isNone() || (!matrix[y + 1][x].isNone() && matrix[y + 1][x].isEmpty()))) {
            count++;
        }

        if (y - 1 >= 0 && (matrix[y - 1][x].isNone() || (!matrix[y - 1][x].isNone() && matrix[y - 1][x].isEmpty()))) {
            count++;
        }

        if (x + 1 <= 8 && (matrix[y][x + 1].isNone() || (!matrix[y][x + 1].isNone() && matrix[y][x + 1].isEmpty()))) {
            count++;
        }

        if (x - 1 >= 0 && (matrix[y][x - 1].isNone() || (!matrix[y][x - 1].isNone() && matrix[y][x - 1].isEmpty()))) {
            count++;
        }

        return count == 4;
    }

    /**
     * Called by {@code checkBoard()} if there are only tiles without any other adjacent tile.
     * Refills the board covering all free slots with new tiles drawn from bag.
     */
    private void refill() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!matrix[i][j].isNone() && matrix[i][j].isEmpty()) {
                    Tile t = bag.draw();
                    if (t.isNone()) {
                        return;
                    } else {
                        t.setpos(i, (char) ('A' + j));
                        matrix[i][j] = (t);
                    }
                }
            }
        }
    }

    /**
     * Checks if the board needs to be refilled and eventually performs a refill.
     */
    public void checkBoard() {
        Logger.debug("checking board");
        boolean ref = true; //tells if a refill is needed
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!getTile(i, j).isNone()) {
                    if (!hasAllFreeSides(i, j)) {
                        ref = false;
                    }
                }
            }
        }
        if (ref) {
            Logger.debug("Refilling");
            refill();
        }
    }


    /**
     * Getter method.
     *
     * @return this {@code Board}'s matrix.
     */
    @Deprecated
    public Tile[][] getMatrix() {  //only for testing purposes
        return matrix;
    }

    /**
     * Get the bag we are using for this game.
     *
     * @return bag.
     */
    public Bag getBag() {
        return bag;
    } //TODO forse non serve

    public void removeTiles(Tile[] tiles) {
        for (Tile t : tiles) {
            int[] index = getTileIndex(t);
            matrix[index[1]][index[0]] = new Tile(TileType.EMPTY);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(matrix);
    }


    /**
     * Gets the {@code Board} of this {@code Game} as a matrix of {@code Tile}.
     *
     * @return the {@code Board}
     */
    public Tile[][] getBoard() {
        Tile[][] board = new Tile[Game.BOARD_DIM][Game.BOARD_DIM];

        for (int i = 0; i < Game.BOARD_DIM; i++) {
            for (int j = 0; j < Game.BOARD_DIM; j++) {
                board[i][j] = matrix[i][j];
            }
        }

        return board;
    }
}
