package it.polimi.ingsw.model.CommonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_4 extends CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_4() {
        id = 4;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        boolean square = false;

        for (int i = 0; i < s.N_ROWS && !square; i++) {
            for (int j = 0; j < s.N_COLS && !square; j++) {
                square = true;
                for (int k = 0; k < 2 && i + k < s.N_ROWS; k++) {
                    for (int m = 0; m < 2 && j + m < s.N_COLS; m++) {
                        if (s.getTile(i, j).type != s.getTile(i + k, j + m).type) {
                            square = false;
                        }
                    }
                }
            }
        }

        if (square) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
