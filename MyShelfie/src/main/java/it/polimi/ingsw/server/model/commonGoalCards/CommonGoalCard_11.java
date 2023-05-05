package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.shelf.Shelf;

public class CommonGoalCard_11 extends CommonGoalCard {

    public CommonGoalCard_11() {
        id = 11;
    }

    @Override
    public int check_objective(Shelf s) {

        for (int i = 1; i < s.N_ROWS - 1; i++) {
            for (int j = 1; j < s.N_COLS - 1; j++) {
                if (s.getTile(i, j).type.equals(s.getTile(i - 1, j - 1).type) &&
                        s.getTile(i, j).type.equals(s.getTile(i + 1, j + 1).type) &&
                        s.getTile(i, j).type.equals(s.getTile(i - 1, j + 1).type) &&
                        s.getTile(i, j).type.equals(s.getTile(i + 1, j - 1).type) &&
                        !s.getTile(i, j).type.isNone()
                ) {
                    return addPoints(s.owner);
                }
            }
        }

        return 0;
    }
}
