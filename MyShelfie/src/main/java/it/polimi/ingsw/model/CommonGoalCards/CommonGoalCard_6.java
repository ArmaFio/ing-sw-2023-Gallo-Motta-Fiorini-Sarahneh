package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_6 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_6() {
        id = 5;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int[] equal;

        equal = new int[Game.N_TYPES];

        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                equal[s.getTile(i, j).type.value()]++;
            }
        }

        for (int e : equal) {
            if (e >= 8) {
                n_solved++;
                return 8 - 2 * (n_solved - 1);
            }
        }

        return 0;
    }
}
