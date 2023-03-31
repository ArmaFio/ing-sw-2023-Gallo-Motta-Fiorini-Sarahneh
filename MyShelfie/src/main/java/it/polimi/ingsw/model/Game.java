package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCards.CommonGoalCard;

import java.util.ArrayList;

public class Game {
    public static final int N_TYPES = 6;
    public static final String PERSONAL_GOALS_PATH = "./src/main/java/it/polimi/ingsw/model/data/personalGoals.ser";
    public static final int N_PERSONAL_GOALS = 12;
    public static final int SHELF_ROWS = 6;
    public static final int SHELF_COLS = 5;
    public static final int END_GAME_TOKEN = 1;
    private final ArrayList<Integer> personalObjs;
    private final Player[] players;

    private final Board board;

    private ArrayList<CommonGoalCard> commonObjs;
    public String winner;

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
                int col = -1; //colonna dove inserire le tessere
                finalPicks = new ArrayList<>();

                //----------
                // come fare per chiedere al client le tiles? Metodo statico in Game?
                //p.pickTiles(board.getAvailableTiles(), finalPicks); //TODO implement pickTiles
                //something to fill finalPicks and get col

                board.removeTiles(finalPicks);
                p.getShelf().put_tiles(col, finalPicks);

                for (CommonGoalCard goal: commonObjs){
                    p.add_points(goal.check_objective(p.getShelf()));
                }

                if (p.getShelf().get_max_coloumns() == 0) {
                    run = false;
                    p.add_points(Game.END_GAME_TOKEN);
                }
            }
        }

        //End-Game
        for(Player p : players){
            p.check_objective();
            p.check_groups();
        }

        int max = players[0].getPoints();
        for (Player p : players){
            if (max <= p.getPoints()){
                max = p.getPoints();
                winner = p.getUsername();
            }
        }

        //TODO comunica al controller che la partita è finita (join)
    }
}