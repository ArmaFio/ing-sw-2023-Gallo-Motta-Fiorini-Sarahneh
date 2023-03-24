package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_5 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_5() {
        id = 5;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int lines, cont;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_TYPES];

        for (int i = 0; i < present.length; i++) {
            present[i] = false;
        }

        for (int i = 0; i < s.N_COLS; i++) {
            cont = 0;
            for (int j = 0; j < s.N_ROWS; j++) {
                if (present[s.getTile(j, i).type.value()]) {
                    cont++;
                } else {
                    present[s.getTile(j, i).type.value()] = true;
                }
            }

            if (cont <= 3) {
                lines++;
            }
        }

        if (lines == 3) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
