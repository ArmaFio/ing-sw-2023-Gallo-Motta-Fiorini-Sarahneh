package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
public class Shelf {
    private  Slot[][] matrix;

    //Constructs a Shelf
    public Shelf() {
        matrix=new Slot [5][6];
        for(int i=0;i<5;i++){
            for(int j=0;j<6;j++){
                matrix[i][j]=new Slot();
            }
        }
    }

    //Returns a list containing the indexes of coloumns which can contain the number of tiles in the argument
    public List avaiable_coloumns(int ntiles){
        int count=0;
        List<Integer> l= new ArrayList<Integer>();
        for(int i=0;i<6;i++){
            count=0;
            for (int j = 0, k = 0; j < 5 && k == 0; j++) {
                if (matrix[j][i].tile == null)
                    count++;
                else
                    k = 1;
            }
            if (count>=ntiles)
                l.add(i);
        }
        return l;
    }

    //Returns the highest number of tiles which can be inserted in the shelf in one turn
    public int get_max_coloumns(){
        int max=0, count=0;
        for(int i=0, done =0;i<6&&done==0;i++) {
            count = 0;
            for (int j = 0, k = 0; j < 5 && k == 0; j++) {
                if (matrix[j][i].tile != null)
                    count++;
                else
                    k = 1;
            }
            if (count > max)
                max = count;
            if (max>=3) {
                max = 3;
                done = 1;
            }


        }
        return max;
    }

    //Puts the assigned tiles in the coloumn having the assigned index
    public void put_tiles (int col, ArrayList<Tile> tiles) {
        for (int i = 4, j = 0; i >= 0 && j == 0; i--) {
            if (matrix[i][col].tile == null) {
                for (int k = 0; k < tiles.size(); k++) {
                    matrix[i - k][col].tile = tiles.get(k);
                }
            }
            j = 1;
        }
    }


    //Returns the tile contained in the [x][y] shelf's slot
    public Tile get_tile(int x, int y){
        return matrix[x][y].tile;
    }

    //Returns the number of rows by which the Shelf is composed
    public int n_row(){
        return 5;
    }

    //Returns the number of coloumns by which the Shelf is composed
    public int n_col(){
        return 6;
    }


    //Assigns a new object to the field 'matrix' of the Shelf
    public void setMatrix(Slot[][] matrix) {
        this.matrix = matrix;
    }
}
