package MyShelfie.src.main.java.it.polimi.ingsw.model;

public abstract class PersonalGoalCard {

     private int points;
     private TileType[][] matrix; //TODO fai lista

     private final int nCol = 5;
     private final int nRow = 6;

   public PersonalGoalCard(){
      matrix = new TileType[nRow][nCol];
      points = 0 ;

    }

    public int getPoints(){

        return points;
    }


    public int check_points (Shelf s ){
        int cont = 0 ;
        for (int i = 0; i < nRow ; i++ ){
            for(int j=0; j< nCol ; j++){

                if (matrix[i][j] == s.matrix[i][j].color){

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
                throw

        }



    }

}
