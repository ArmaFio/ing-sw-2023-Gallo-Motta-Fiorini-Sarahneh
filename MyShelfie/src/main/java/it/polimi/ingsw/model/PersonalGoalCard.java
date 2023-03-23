package MyShelfie.src.main.java.it.polimi.ingsw.model;

import java.util.HashMap;
import java.util.Hashtable;

public abstract class PersonalGoalCard {

     private int points;
     private TileType[][] matrix; //TODO fai lista



   public PersonalGoalCard(HashMap<String,int[]> dict){
       int nCol= 5; //TODO aggiusta
       int nRow= 6;
       matrix = new TileType[nRow][nCol];
       points = 0 ;
       for(String key: dict.keySet()){
           matrix[dict.get(key)[0]][dict.get(key)[1]]=key // TODO per il momento aspettiamo Samuele per il TileType

       }
    }

    public int getPoints(){

        return points;
    }


    public int check_points (Shelf s ){
        int cont = 0 ;
        for (int i = 0; i < s.nRow ; i++ ){
            for(int j=0; j< s.nCol ; j++){

                if (matrix[i][j] == s.getTile(i,j).color){

                    cont++;

                }
            }
        }
        switch (cont){
            case 0:
                return 0;
                break;
            case 1:
                return 1;
            break;
            case 2:
                return 2;
            break;
            case 3:
                return 4;
            break;
            case 4:
                return 6;
            break;
            case 5:
                return 9;
            break;
            case 6:
                return 12;
            break;

            default:
                System.out.println("error: PersonalGoalCard");

        }



    }

}
