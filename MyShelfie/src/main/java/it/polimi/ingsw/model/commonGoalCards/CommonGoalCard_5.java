package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_5 extends CommonGoalCard {
    public CommonGoalCard_5() {
        id = 5;
    }

    @Override
    public int check_objective(Shelf s) {
        int lines, cont;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_TYPES + 1];

        for (int i = 0; i < s.N_COLS; i++) {
            cont = 0;

            for (int j = 0; j < present.length; j++) {
                present[j] = false;
            }

            for (int j = 0; j < s.N_ROWS; j++) {
                if (!present[s.getTile(j, i).type.value()]) {
                    cont++;
                    present[s.getTile(j, i).type.value()] = true;
                }
            }

            if (cont <= 3 && !present[TileType.NONE.value()]) {
                lines++;
            }
        }

        if (lines >= 3) {
            return addPoints();
        }

        return 0;
    }
}
