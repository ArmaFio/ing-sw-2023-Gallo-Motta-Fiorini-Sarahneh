package it.polimi.ingsw.server.model.GameT;


import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard_1;
import it.polimi.ingsw.server.model.commonGoalCards.CommonGoalCard_3;
import it.polimi.ingsw.server.model.shelf.Shelf;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTest {
    static class BoardBuilder extends Board {
        public static int[][] startingBoard = new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 2, 2, 0, 0, 0, 0},
                {0, 0, 0, 2, 2, 2, 0, 0, 0},
                {0, 0, 2, 2, 2, 2, 2, 2, 0},
                {0, 2, 2, 2, 2, 2, 2, 2, 0},
                {0, 2, 2, 2, 2, 2, 2, 0, 0},
                {0, 0, 0, 2, 2, 2, 0, 0, 0},
                {0, 0, 0, 0, 7, 2, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        public final Tile[][] matrix;

        public BoardBuilder() {
            super(2, new TilesBag());
            int k = 0;
            matrix = new Tile[startingBoard.length][startingBoard.length];
            for (int i = 0; i < startingBoard.length; i++) {
                for (int j = 0; j < startingBoard[i].length; j++) {
                    if (startingBoard[i][j] == 0) {
                        matrix[i][j] = new Tile(TileType.NONE);
                    }
                    else if(startingBoard[i][j]==7){
                        matrix[i][j]=new Tile(TileType.toEnum(startingBoard[i][j]),999);
                    }
                    else {
                        matrix[i][j] = new Tile(TileType.toEnum(startingBoard[i][j]), k);
                        k++;
                    }
                }
            }
        }
        public void removeTiles(Tile[] tiles) {
            for (Tile t : tiles) {
                int[] index = getTileIndex(t);
                matrix[index[1]][index[0]] = new Tile(TileType.EMPTY);
            }
        }
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

    }

    @Test
    public void NextTurnTest(){
        String[] usernames = {"a", "b"};
        CommonGoalCard_1 c1 = new CommonGoalCard_1(4);
        CommonGoalCard_3 c2 = new CommonGoalCard_3(4);
        int[][] matrix = new int[][]{
                {0, 6, 6, 0, 6},
                {1, 0, 0, 0, 1},
                {4, 4, 0, 2, 1},
                {1, 3, 0, 2, 0},
                {1, 3, 0, 0, 0},
                {6, 0, 5, 2, 6}
        };
        int[][] newmatrix = new int[][]{
                {6, 6, 6, 0, 6},
                {1, 0, 0, 0, 1},
                {4, 4, 0, 2, 1},
                {1, 3, 0, 2, 0},
                {1, 3, 0, 0, 0},
                {6, 0, 5, 2, 6}
        };
        Shelf sh=convert_to_shelf(matrix);
        TestableGame g= new TestableGame(usernames,0);
        g.getPlayer("a").getShelfObj().setMatrix(sh.getMatrix());
        g.board=new BoardBuilder();
        Tile[] picked= {new Tile(TileType.toEnum(7),999)};
        g.commonGoals= new CommonGoalCard[]{c1, c2};
        g.nextTurn("a",picked,0);
        assertTrue(g.board.matrix[7][4].isEmpty());
        assertFalse(g.isEnded());
        assertSame(g.getPlayer("a").getShelf()[0][0].type, TileType.PLANT);
        for(int i=0; i<6; i++){
            for(int j=0; j<5;j++) {
                if(i==0&&j==0)
                    assertTrue(g.getPlayer("a").getShelf()[i][j].id==999);
                else
                    assertTrue(g.getPlayer("a").getShelf()[i][j].equalsId(convert_to_shelf(newmatrix).getShelf()[i][j]));
                assertTrue(g.getPlayer("a").getShelf()[i][j].equalsType(convert_to_shelf(newmatrix).getShelf()[i][j]));
            }
        }
        assertEquals(16, g.getPlayer("a").getPoints());
        g.endGame();
        assertEquals(27, g.getPlayer("a").getPoints());
        assertEquals(g.winner, "a");

    }

    Shelf convert_to_shelf(int[][] matrix) {
        Shelf s = new Shelf("Test");
        Tile[][] slots = new Tile[Game.SHELF_ROWS][Game.SHELF_COLS];

        for (int i = 0; i < Game.SHELF_ROWS; i++) {
            for (int j = 0; j < Game.SHELF_COLS; j++) {
                slots[i][j] = new Tile(TileType.toEnum(matrix[i][j]+1));
            }
        }

        s.setMatrix(slots);

        return s;
    }
}