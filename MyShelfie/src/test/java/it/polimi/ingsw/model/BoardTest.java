package it.polimi.ingsw.model;

import it.polimi.ingsw.server.model.Bag;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.BoardSlot;
import it.polimi.ingsw.server.model.Tile;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;

public class BoardTest {
    @Test
    public void initializeBoardForTwoPlayers() {
        int[][] conf = new int[][]{ //board configuration
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        int[][] result = new int[9][9];
        Bag bag = new Bag();
        Board board = new Board(2, bag);
        BoardSlot[][] created = board.getMatrix();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(created[i][j].isUsable()){
                    result[i][j] = 1;
                }else{
                    result[i][j] = 0;
                }
            }
        }
        assertArrayEquals(conf,result);
    }

    @Test
    public void initializeBoardForThreePlayers() {
        int[][] conf = new int[][]{ //board configuration
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0}
        };
        int[][] result = new int[9][9];
        Bag bag = new Bag();
        Board board = new Board(3, bag);
        BoardSlot[][] created = board.getMatrix();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(created[i][j].isUsable()){
                    result[i][j] = 1;
                }else{
                    result[i][j] = 0;
                }
            }
        }
        assertArrayEquals(conf,result);
    }

    @Test
    public void initializeBoardForFourPlayers() {
        int[][] conf = new int[][]{ //board configuration
                {0, 0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 1, 1, 0, 0, 0}
        };
        int[][] result = new int[9][9];
        Bag bag = new Bag();
        Board board = new Board(4, bag);
        BoardSlot[][] created = board.getMatrix();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(created[i][j].isUsable()){
                    result[i][j] = 1;
                }else{
                    result[i][j] = 0;
                }
            }
        }
        assertArrayEquals(conf,result);
    }

    @Test
    public void initializedBag() {
        int count = 0;
        Bag bag = new Bag();
        ArrayList<Tile> tiles = bag.getTiles();
        for (int i = 0; i < tiles.size(); i++) {
            count++;
            System.out.println(tiles.get(i).type);
        }
        System.out.println("Count: " + count);
    }

    @Test
    public void checkDraw() {
        int count = 0;
        Bag bag = new Bag();
        ArrayList<Tile> tiles = bag.getTiles();
        Board board = new Board(4, bag);
        System.out.println("Remaining tiles:");
        for (int i = 0; i < tiles.size(); i++) {
            count++;
            System.out.println(tiles.get(i).type);
        }
        System.out.println("Count: " + count);
    }

    @Test
    public void checkPositioningAndFree() {
        Bag bag = new Bag();
        Board board = new Board(2, bag);
        BoardSlot[][] matrix = board.getMatrix();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (matrix[i][j].getTile() != null) {
                    System.out.print(" " + matrix[i][j].getTile().type);
                } else {
                    System.out.print(" 0");
                }
            }
            System.out.println("\n");
        }
        ArrayList<Tile> available = board.getSomeAvailableTiles();
        System.out.println("Available tiles:");
        for (Tile tile : available) {
            System.out.println(tile.type);
        }
    }

    @Test
    public void checkFindTilesToPick4Players() {  //change players to see changes
        int[][] result = new int[9][9];
        ArrayList<Tile> av1 = new ArrayList<>();
        ArrayList<Tile> av2 = new ArrayList<>();
        ArrayList<Tile> av3 = new ArrayList<>();
        Bag bag = new Bag();
        Board board = new Board(4, bag);
        BoardSlot[][] created = board.getMatrix();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (created[i][j].isUsable()) {
                    result[i][j] = 1;
                }else{
                    result[i][j] = 0;
                }
            }
        }
        System.out.println("Initial board:");
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(result[i][j] + "  ");
            }
            System.out.print("\n");
        }
        int[][] available = new int[9][9];
        av1 = board.getSomeAvailableTiles();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if (created[i][j].isUsable() && av1.contains(created[i][j].getTile())) {
                    available[i][j] = 1;
                    //av1.remove(created[i][j].getSlotTile());
                } else {
                    available[i][j] = 0;
                }
            }
        }
        System.out.println("Filtered for available at first pick");
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(available[i][j] + "  ");
            }
            System.out.print("\n");
        }
        for(int y = 0; y < 9; y++){
            for(int x = 0; x < 9; x++){
                if(created[y][x].isUsable() && board.hasFreeSide(x, y)){
                    av2 = board.getSomeAvailableTiles(created[y][x].getTile());
                    for(int i = 0; i < 9; i++){
                        for(int j = 0; j < 9; j++){
                            if (created[i][j].isUsable() && av2.contains(created[i][j].getTile())) {
                                available[i][j] = 2;
                                //av2.remove(created[i][j].getSlotTile());
                            } else {
                                available[i][j] = 0;
                            }
                        }
                    }
                    available[y][x] = 1;
                    System.out.println("Filtered for available after first pick in [" + y + "][" + x + "]");
                    for(int i = 0; i < 9; i++){
                        for(int j = 0; j < 9; j++){
                            System.out.print(available[i][j] + "  ");
                        }
                        System.out.print("\n");
                    }
                    for(Tile t2 : av2){
                        av3 = board.getSomeAvailableTiles(created[y][x].getTile(), t2);
                        System.out.println("Size of the list: " + av3.size());
                        for(int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                if (created[i][j].isUsable() && av3.contains(created[i][j].getTile())) {
                                    available[i][j] = 3;
                                    //av2.remove(created[i][j].getSlotTile());
                                }
                            }
                        }
                        System.out.println("Filtered for available after second pick in [" + y + "][" + x + "]");
                        for(int i = 0; i < 9; i++){
                            for(int j = 0; j < 9; j++){
                                System.out.print(available[i][j] + "  ");
                            }
                            System.out.print("\n");
                        }
                    }
                }
            }
        }
    }

    @Test
    public void checkAvailableTilesPersonalConfiguration() { //this case has a row of 3 tiles available
        int[][] result = new int[9][9];
        ArrayList<Tile> av1 = new ArrayList<>();
        ArrayList<Tile> av2 = new ArrayList<>();
        ArrayList<Tile> av3 = new ArrayList<>();
        int[][] conf = new int[][]{ //board configuration
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
        BoardSlot[][] created = board.getMatrix();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(conf[i][j] == 9){
                    created[i][j].setTaken(true);
                }
            }
        }
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if (created[i][j].isUsable() && !created[i][j].isOccupied()) {
                    result[i][j] = 1;
                } else {
                    result[i][j] = 0;
                }
            }
        }
        System.out.println("Initial board:");
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(result[i][j] + "  ");
            }
            System.out.print("\n");
        }
        int[][] available = new int[9][9];
        av1 = board.getSomeAvailableTiles();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if (av1.contains(created[i][j].getTile())) {
                    available[i][j] = 1;
                    //av1.remove(created[i][j].getSlotTile());
                } else {
                    available[i][j] = 0;
                }
            }
        }
        System.out.println("Filtered for available at first pick");
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                System.out.print(available[i][j] + "  ");
            }
            System.out.print("\n");
        }
        for(int y = 0; y < 9; y++){
            for(int x = 0; x < 9; x++) {
                if (av1.contains(created[y][x].getTile())) {
                    av2 = board.getSomeAvailableTiles(created[y][x].getTile());
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (av2.contains(created[i][j].getTile())) {
                                available[i][j] = 2;
                                //av2.remove(created[i][j].getSlotTile());
                            } else {
                                available[i][j] = 0;
                            }
                        }
                    }
                    available[y][x] = 1;
                    System.out.println("Filtered for available after first pick in [" + y + "][" + x + "]");
                    for(int i = 0; i < 9; i++){
                        for(int j = 0; j < 9; j++){
                            System.out.print(available[i][j] + "  ");
                        }
                        System.out.print("\n");
                    }
                    System.out.println("av2 size: " + av2.size());
                    for(Tile t2 : av2){
                        av3 = board.getSomeAvailableTiles(created[y][x].getTile(), t2);
                        System.out.println("Size of the list: " + av3.size());
                        for(int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                if (created[i][j].isUsable() && av3.contains(created[i][j].getTile())) {
                                    available[i][j] = 3;
                                    //av2.remove(created[i][j].getSlotTile());
                                }
                            }
                        }
                        System.out.println("Filtered for available after second pick in [" + y + "][" + x + "]");
                        for(int i = 0; i < 9; i++){
                            for(int j = 0; j < 9; j++){
                                System.out.print(available[i][j] + "  ");
                            }
                            System.out.print("\n");
                        }
                    }
                }
            }
        }
    }

    @Test
    public void boh() { //TODO cambia nome (Gallo Matteo si dissocia)
        int[][] result = new int[9][9];
        ArrayList<Tile> av1 = new ArrayList<>();
        ArrayList<Tile> av2 = new ArrayList<>();
        ArrayList<Tile> av3 = new ArrayList<>();
        int[][] conf = new int[][]{ //board configuration
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
        BoardSlot[][] created = board.getMatrix();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(conf[i][j] == 9){
                    created[i][j].setTaken(true);
                }
            }
        }
        System.out.println("Free side [4][1]: " + board.hasFreeSide(1, 4));
    }

}