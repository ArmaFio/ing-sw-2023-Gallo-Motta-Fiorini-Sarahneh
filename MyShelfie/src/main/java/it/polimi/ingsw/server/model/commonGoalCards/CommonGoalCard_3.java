package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.shelf.Shelf;

import java.util.ArrayList;

public class CommonGoalCard_3 extends CommonGoalCard {
    public CommonGoalCard_3() {
        id = 3;
        this.solvers = new ArrayList<>();
    }

    @Override
    public int check_objective(Shelf s) {
        if (s.getTile(0, 0).type.equals(s.getTile(0, s.N_COLS - 1).type) &&
                s.getTile(0, 0).type.equals(s.getTile(s.N_ROWS - 1, s.N_COLS - 1).type) &&
                s.getTile(0, 0).type.equals(s.getTile(s.N_ROWS - 1, 0).type) &&
                !s.getTile(0, 0).isNone()
        ) {
            return addPoints(s.owner);
        }

        return 0;
    }
}
