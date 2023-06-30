package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Shelf;

public class CommonGoalCard_7 extends CommonGoalCard {

    public CommonGoalCard_7(int nPlayers) {
        super(nPlayers);
        id = 7;
        this.description = "Five tiles of the same type forming a " +
                "diagonal. ";
    }

    @Override
    public int check_objective(Shelf s) {
        boolean diag_a, diag_b, diag_c, diag_d;

        diag_a = diag_b = diag_c = diag_d = true;

        for (int i = 0; i < s.N_COLS; i++) {
            if (s.getTile(0, 0).isEmpty() || !s.getTile(0, 0).equalsType(s.getTile(i, i))) {
                diag_a = false;
            }
            if (s.getTile(1, 0).isEmpty() || !s.getTile(1, 0).equalsType(s.getTile(i + 1, i))) {
                diag_b = false;
            }
            if (s.getTile(0, s.N_COLS - 1).isEmpty() || !s.getTile(0, s.N_COLS - 1).equalsType(s.getTile(i, s.N_COLS - i - 1))) {
                diag_c = false;
            }
            if (s.getTile(1, s.N_COLS - 1).isEmpty() || !s.getTile(1, s.N_COLS - 1).equalsType(s.getTile(i + 1, s.N_COLS - i - 1))) {
                diag_d = false;
            }
        }

        if (diag_a || diag_b || diag_c || diag_d) {
            return addPoints(s.owner);
        }

        return 0;
    }
}
