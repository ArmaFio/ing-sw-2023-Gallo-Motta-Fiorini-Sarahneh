package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_8 extends CommonGoalCard {
    public CommonGoalCard_8() {
        id = 8;
    }

    @Override
    public int check_objective(Shelf s) {
        int lines, cont;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_TYPES + 1];

        for (int i = 0; i < s.N_ROWS; i++) {
            cont = 0;

            for (int j = 0; j < present.length; j++) {
                present[j] = false;
            }

            for (int j = 0; j < s.N_COLS; j++) {
                if (!present[s.getTile(i, j).type.value()]) {
                    present[s.getTile(i, j).type.value()] = true;
                    cont++;
                }
            }

            if (cont <= 3 && !present[TileType.NONE.value()]) {
                lines++;
            }
        }

        if (lines >= 4) {
            return addPoints();
        }

        return 0;
    }
}
