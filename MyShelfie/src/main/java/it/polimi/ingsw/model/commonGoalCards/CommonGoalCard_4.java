package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_4 extends CommonGoalCard {

    public CommonGoalCard_4() {
        id = 4;
    }

    @Override
    public int check_objective(Shelf s) {
        boolean square;
        int cont = 0;

        for (int i = 0; i < s.N_ROWS - 1; i++) {
            for (int j = 0; j < s.N_COLS - 1; j++) {
                square = true;

                for (int k = 0; k < 2; k++) {
                    for (int m = 0; m < 2; m++) {
                        if (s.getTile(i, j).type.isNone() || !s.getTile(i, j).type.equals(s.getTile(i + k, j + m).type)) {
                            square = false;
                        }
                    }
                }

                if (square) {
                    cont++;
                }
            }
        }

        if (cont >= 2) {
            return addPoints();
        }

        return 0;
    }
}
