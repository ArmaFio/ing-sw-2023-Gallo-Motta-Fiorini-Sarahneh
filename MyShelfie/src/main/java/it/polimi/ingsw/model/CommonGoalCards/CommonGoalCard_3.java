package it.polimi.ingsw.model.CommonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_3 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_3() {
        id = 3;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        if (s.getTile(0, 0).type == s.getTile(0, s.N_COLS - 1).type &&
                s.getTile(0, 0).type == s.getTile(s.N_ROWS - 1, s.N_COLS - 1).type &&
                s.getTile(0, 0).type == s.getTile(s.N_ROWS - 1, 0).type
        ) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
