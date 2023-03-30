package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;

import java.util.ArrayList;

public class Game {
    public static final int N_TYPES = 6;
    public static final String PERSONAL_GOALS_PATH = "./src/main/java/it/polimi/ingsw/model/data/personalGoals.ser";
    public static final int N_PERSONAL_GOALS = 12;
    public static final int SHELF_ROWS = 6;
    public static final int SHELF_COLS = 5;
    private final ArrayList<Integer> personalObjs;
    private final Player[] players;

    private final Board board;

    private ArrayList<CommonGoalCard> commonObjs;

    //TODO end game logic
    public Game(ArrayList<String> users) {
        personalObjs = new ArrayList<>();
        for (int i = 0; i < N_PERSONAL_GOALS; i++) {
            personalObjs.add(i);
        }
        players = new Player[users.size()];
        for (int i = 0; i < users.size(); i++) {
            players[i] = new Player(users.get(i), new PersonalGoalCard(personalObjs));
        }

        board = new Board(users.size(), new Bag());

        commonObjs = new CommonBag().draw();
    }

    public void runGame() {
        boolean run = true;
        ArrayList<Tile> finalPicks; //tiles chosen by the player already in order

        while (run) { //end game condition
            for (Player p : players) {
                int col = -1;
                finalPicks = new ArrayList<>();
                if (p.getShelf().get_max_coloumns() == 0) {
                    run = false;
                    break;  //end game
                }
                //----------
                // come fare per chiedere al client le tiles? Metodo statico in Game?
                //p.pickTiles(board.getAvailableTiles(), finalPicks); //TODO implement pickTiles
                //something to fill finalPicks and get col

                p.getShelf().put_tiles(col, finalPicks);
                board.removeTiles(finalPicks);

            }
        }
    }
}

