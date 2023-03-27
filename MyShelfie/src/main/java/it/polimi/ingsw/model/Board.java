package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Board {
    private final BoardSlot[][] matrix;
    private int players; //number of players for this board
    private int nTiles; //number of tiles currently on the board
    private Bag bag;  //bag used for this game
    private final int[][] conf = new int[][]{ //board configuration
            {0, 0, 0, 3, 4, 0, 0, 0, 0},
            {0, 0, 0, 2, 2, 4, 0, 0, 0},
            {0, 0, 3, 2, 2, 2, 3, 0, 0},
            {0, 4, 2, 2, 2, 2, 2, 2, 3},
            {4, 2, 2, 2, 2, 2, 2, 2, 4},
            {3, 2, 2, 2, 2, 2, 2, 4, 0},
            {0, 0, 3, 2, 2, 2, 3, 0, 0},
            {0, 0, 0, 4, 2, 2, 0, 0, 0},
            {0, 0, 0, 0, 4, 3, 0, 0, 0}
    };

    /**
     * Constructor: initialize the board assigning tiles picked from bag to slots.
     * @param players number of players.
     * @param bag bag from which tiles are going to be drawn for the entire game.
     */
    public Board(int players, Bag bag){
        nTiles = 0;
        this.bag = bag;
        this.players = players;
        matrix = new BoardSlot[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(conf[i][j] == 0){
                    matrix[i][j] = new BoardSlot(false, bag);
                }else{
                    if( players >= conf[i][j]){
                        matrix[i][j] = new BoardSlot(true, bag);
                        nTiles++;
                    }else{
                        matrix[i][j] = new BoardSlot(false, bag);
                        nTiles++;
                    }
                }
            }
        }
    }

    /**
     * Get the reference of the tile from the board.
     * @param x row of the board.
     * @param y column of the board.
     * @return reference to the tile located in matrix[x][y].
     */
    public Tile getTile(int x, int y){return matrix[y][x].getSlotTile();}

    /**
     * Getter method.
     * @return ArrayList of tiles that can be picked up as first tile at the beginning of player's turn.
     */
    public ArrayList<Tile> getAvailableTiles(){
        ArrayList<Tile> availableTiles = new ArrayList<Tile>();
        //look for available tiles on the board
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(matrix[i][j].isUsable() && !matrix[i][j].occupied()){
                    if(hasFreeSide(i, j)){
                        availableTiles.add(matrix[i][j].getSlotTile());
                    }
                }
            }
        }
        return availableTiles;
    }

    /**
     * Called by the game logic if the player decides to pick a second tile.
     * @param tile first tile chosen by the player in this turn.
     * @return all the possible tiles you can pick as second tile in this turn.
     */
    public ArrayList<Tile> getAvailableTiles(Tile tile){
        ArrayList<Tile> availableTiles = new ArrayList<Tile>();
        int[] index = getTileIndex(tile);
        if(index[1]-1 >= 0 && matrix[index[1]-1][index[0]].isUsable() && !matrix[index[1]-1][index[0]].occupied() && hasFreeSide(index[0], index[1]-1)){
            availableTiles.add(matrix[index[1]-1][index[0]].getSlotTile());
        }

        if(index[1]+1 <= 8 && matrix[index[1]+1][index[0]].isUsable() && !matrix[index[1]+1][index[0]].occupied() && hasFreeSide(index[0], index[1]+1)){
            availableTiles.add(matrix[index[1]+1][index[0]].getSlotTile());
        }

        if(index[0]-1 >= 0 && matrix[index[1]][index[0]-1].isUsable() && !matrix[index[1]][index[0]-1].occupied() && hasFreeSide(index[0]-1, index[1])){
            availableTiles.add(matrix[index[1]][index[0]-1].getSlotTile());
        }

        if(index[0]+1 <= 8 && matrix[index[1]][index[0]+1].isUsable() && !matrix[index[1]][index[0]+1].occupied() && hasFreeSide(index[0]+1, index[1])){
            availableTiles.add(matrix[index[1]][index[0]+1].getSlotTile());
        }

        return availableTiles;
    }

    /**
     * Called by the game logic if the player decides to pick a third tile.
     * @param t1 picked tile
     * @param t2 picked tile
     * @return all the possible tiles you can pick as third tile in this turn.
     */
    public ArrayList<Tile> getAvailableTiles(Tile t1, Tile t2){
        ArrayList<Tile> availableTiles = new ArrayList<Tile>();
        int[] index1 = getTileIndex(t1);
        int[] index2 = getTileIndex(t2);
        if(index1[0] == index2[0]){ //same column
            switch (index1[1] - index2[1]) {
                case -1 -> {  //t1 above t2
                    if (index1[1] - 1 >= 0 && matrix[index1[1] - 1][index1[0]].isUsable() && !matrix[index1[1] - 1][index1[0]].occupied() && hasFreeSide(index1[0], index1[1] - 1)) {
                        availableTiles.add(matrix[index1[1] - 1][index1[0]].getSlotTile());
                    }
                    if (index2[1] + 1 <= 8 && matrix[index2[1] + 1][index2[0]].isUsable() && !matrix[index2[1] + 1][index2[0]].occupied() && hasFreeSide(index2[0], index2[1] + 1)) {
                        availableTiles.add(matrix[index2[1] + 1][index2[0]].getSlotTile());
                    }
                }
                case 1 -> { //t2 above t1
                    if (index2[1] - 1 >= 0 && matrix[index2[1] - 1][index2[0]].isUsable() && !matrix[index2[1] - 1][index2[0]].occupied() && hasFreeSide(index2[0], index2[1] - 1)) {
                        availableTiles.add(matrix[index2[1] - 1][index2[0]].getSlotTile());
                    }
                    if (index1[1] + 1 <= 8 && matrix[index1[1] + 1][index1[0]].isUsable() && !matrix[index1[1] + 1][index1[0]].occupied() && hasFreeSide(index1[0], index1[1] + 1)) {
                        availableTiles.add(matrix[index1[1] + 1][index1[0]].getSlotTile());
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + (index1[1] - index2[1]));
            }
            return availableTiles;
        }

        if(index1[1] == index2[1]){ //same row
            switch (index1[0] - index2[0]) {
                case 1 -> { //t2 before t1
                    if (index1[0] + 1 <= 8 && matrix[index1[1]][index1[0] + 1].isUsable() && !matrix[index1[1]][index1[0] + 1].occupied() && hasFreeSide(index1[0] + 1, index1[1])) {
                        availableTiles.add(matrix[index1[1]][index1[0] + 1].getSlotTile());
                    }
                    if (index2[0] - 1 >= 0 && matrix[index2[1]][index2[0] - 1].isUsable() && !matrix[index2[1]][index2[0] - 1].occupied() && hasFreeSide(index2[0] - 1, index2[1])) {
                        availableTiles.add(matrix[index2[1]][index2[0] - 1].getSlotTile());
                    }
                }
                case -1 -> { //t1 before t2
                    if (index1[0] - 1 >= 0 && matrix[index1[1]][index1[0] - 1].isUsable() && !matrix[index1[1]][index1[0] - 1].occupied() && hasFreeSide(index1[0] - 1, index1[1])) {
                        availableTiles.add(matrix[index1[1]][index1[0] - 1].getSlotTile());
                    }
                    if (index2[0] + 1 <= 8 && matrix[index2[1]][index2[0] + 1].isUsable() && !matrix[index2[1]][index2[0] + 1].occupied() && hasFreeSide(index2[0] + 1, index2[1])) {
                        availableTiles.add(matrix[index2[1]][index2[0] + 1].getSlotTile());
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
     * @param tile the tile I want to know the position in the board.
     * @return an index[2] containing the column and the row (in this order).
     */
    private int[] getTileIndex(Tile tile){
        int[] index = new int[2];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(matrix[i][j].getSlotTile() == tile){
                    index[0] = j;  //x
                    index[1] = i;  //y
                }
            }
        }
        return  index;
    }


    /**
     * Verify if the tile in the [y][x] position has at least one free side.
     * @param y row
     * @param x column
     * @return true if the tile has a free side, false otherwise.
     */
    public boolean hasFreeSide(int y, int x){  //made public for test purposes, valuate to set as private after testing
        if(y-1 < 0 || y+1 > 8 || x-1 < 0 || x+1 > 8){  //check edge position cases
            return true;
        }
        if(!matrix[y+1][x].isUsable() || (matrix[y+1][x].isUsable() && matrix[y+1][x].occupied())){
            return true;
        }

        if(!matrix[y-1][x].isUsable() || (matrix[y-1][x].isUsable() && matrix[y-1][x].occupied())){
            return true;
        }

        if(!matrix[y][x+1].isUsable() || (matrix[y][x+1].isUsable() && matrix[y][x+1].occupied())){
            return true;
        }

        if(!matrix[y][x-1].isUsable() || (matrix[y][x-1].isUsable() && matrix[y][x-1].occupied())){
            return true;
        }
        return false;
    }

    public boolean hasAllFreeSides(int y, int x){  //made public for test purposes, valuate to set as private after testing
        int count = 0;
        if(y - 1 < 0){  //check edge position cases
            count++;
        }
        if(y + 1 > 8){  //check edge position cases
            count++;
        }
        if(x - 1 < 0){  //check edge position cases
            count++;
        }
        if(x + 1 > 8){  //check edge position cases
            count++;
        }
        if(y + 1 <= 8 && (!matrix[y+1][x].isUsable() || (matrix[y+1][x].isUsable() && matrix[y+1][x].occupied()))){
            count++;
        }

        if(y - 1 >= 0 && (!matrix[y-1][x].isUsable() || (matrix[y-1][x].isUsable() && matrix[y-1][x].occupied()))){
            count++;
        }

        if(x + 1 <= 8 && (!matrix[y][x+1].isUsable() || (matrix[y][x+1].isUsable() && matrix[y][x+1].occupied()))){
            count++;
        }

        if(x - 1 >= 0 && (!matrix[y][x-1].isUsable() || (matrix[y][x-1].isUsable() && matrix[y][x-1].occupied()))){
            count++;
        }

        if(count == 4){
            return true;
        }
        return false;
    }

    /**
     * Called by {@code checkBoard()} if there are only tiles without any other adjacent tile.
     * Refills the board covering all free slots with new tiles drawn from bag.
     * @param bag bag used for this game.
     */
    private void refill(Bag bag){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(matrix[i][j].isUsable() && matrix[i][j].occupied()){
                    matrix[i][j] = new BoardSlot(true, bag);
                }
            }
        }
    }

    /**
     * Checks if the board needs to be refilled and eventually performs a refill.
     */
    public void checkBoard(){
        boolean ref = true; //tells if a refill is needed
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(!hasAllFreeSides(i, j)){
                    ref = false;
                }
            }
        }
        if(ref){
            refill(bag);
        }
    }
    public BoardSlot[][] getMatrix(){  //only for testing purposes
        return matrix;
    }

    /**
     * Get the bag we are using for this game.
     * @return bag.
     */
    public Bag getBag(){
        return bag;
    }
}
