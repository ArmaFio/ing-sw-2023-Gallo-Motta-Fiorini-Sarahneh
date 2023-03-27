package it.polimi.ingsw.model.CommonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.Tile;

public class CommonGoalCard_11 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_11() {
        id = 11;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {

        for (int i = 1; i < s.N_ROWS - 1; i++) {
            for (int j = 1; j < s.N_COLS - 1; j++) {
                if (s.getTile(i, j).type == s.getTile(i - 1, j - 1).type &&
                        s.getTile(i, j).type == s.getTile(i + 1, j + 1).type &&
                        s.getTile(i, j).type == s.getTile(i - 1, j + 1).type &&
                        s.getTile(i, j).type == s.getTile(i + 1, j - 1).type
                ) {
                    n_solved++;
                    return 8 - 2 * (n_solved - 1);
                }
            }
        }

        return 0;
    }
}
