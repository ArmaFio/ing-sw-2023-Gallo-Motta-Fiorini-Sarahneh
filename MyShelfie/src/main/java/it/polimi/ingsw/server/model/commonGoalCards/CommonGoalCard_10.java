package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.server.model.shelf.Shelf;

import java.util.ArrayList;

public class CommonGoalCard_10 extends CommonGoalCard {
    public CommonGoalCard_10() {
        id = 10;
        this.solvers = new ArrayList<>();
    }

    @Override
    public int check_objective(Shelf s) {
        int lines;
        boolean cond;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_TYPES + 1];

        for (int i = 0; i < s.N_ROWS; i++) {
            cond = true;

            for (int j = 0; j < present.length; j++) {
                present[j] = false;
            }

            for (int j = 0; j < s.N_COLS; j++) {
                if (present[s.getTile(i, j).type.value()]) {
                    cond = false;
                }
                present[s.getTile(i, j).type.value()] = true;
            }

            if (cond && !present[TileType.EMPTY.value()]) {
                lines++;
            }
        }

        if (lines >= 2) {
            return addPoints(s.owner);
        }

        return 0;
    }
}
