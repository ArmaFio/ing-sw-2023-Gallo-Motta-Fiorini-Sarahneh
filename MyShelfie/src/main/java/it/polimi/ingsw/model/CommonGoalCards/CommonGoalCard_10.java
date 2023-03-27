package it.polimi.ingsw.model.CommonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_10 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_10() {
        id = 10;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int lines;
        boolean cond;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_TYPES];

        for (int i = 0; i < present.length; i++) {
            present[i] = false;
        }

        for (int i = 0; i < s.N_ROWS; i++) {
            cond = true;
            for (int j = 0; j < s.N_COLS; j++) {
                if (present[s.getTile(i, j).type.value()]) {
                    cond = false;
                }
                present[s.getTile(i, j).type.value()] = true;
            }

            if (cond) {
                lines++;
            }
        }

        if (lines == 2) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
