package it.polimi.ingsw.model.CommonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_7 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_7() {
        id = 7;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        boolean diag_a, diag_b, diag_c, diag_d;

        diag_a = diag_b = diag_c = diag_d = true;

        for (int i = 0; i < s.N_ROWS; i++) {
            if (s.getTile(0, 0).type != s.getTile(i, i).type) {
                diag_a = false;
            }
            if (s.getTile(0, 1).type != s.getTile(i, i + 1).type) {
                diag_b = false;
            }
            if (s.getTile(0, s.N_COLS).type != s.getTile(i, s.N_COLS - i).type) {
                diag_c = false;
            }
            if (s.getTile(0, s.N_COLS + 1).type != s.getTile(i + 1, s.N_COLS - i).type) {
                diag_d = false;
            }
        }

        if (diag_a || diag_b || diag_c || diag_d) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
