package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_6 extends CommonGoalCard {
    public CommonGoalCard_6() {
        id = 5;
    }

    @Override
    public int check_objective(Shelf s) {
        int[] equal;

        equal = new int[Game.N_TYPES + 1];

        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                equal[s.getTile(i, j).type.value()]++;
            }
        }

        for (int i = 1; i < equal.length; i++) {
            if (equal[i] >= 8) {
                return addPoints();
            }
        }

        return 0;
    }
}
