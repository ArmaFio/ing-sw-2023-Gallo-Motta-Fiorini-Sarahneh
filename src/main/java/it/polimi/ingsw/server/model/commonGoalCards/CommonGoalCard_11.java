package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.shelf.Shelf;

public class CommonGoalCard_11 extends CommonGoalCard {

    public CommonGoalCard_11(int nPlayers) {
        super(nPlayers);
        id = 11;
        this.description = "Five tiles of the same type forming an X.";
    }

    @Override
    public int check_objective(Shelf s) {

        for (int i = 1; i < s.N_ROWS - 1; i++) {
            for (int j = 1; j < s.N_COLS - 1; j++) {
                if (s.getTile(i, j).equalsType(s.getTile(i - 1, j - 1)) &&
                        s.getTile(i, j).equalsType(s.getTile(i + 1, j + 1)) &&
                        s.getTile(i, j).equalsType(s.getTile(i - 1, j + 1)) &&
                        s.getTile(i, j).equalsType(s.getTile(i + 1, j - 1)) &&
                        !s.getTile(i, j).isEmpty()
                ) {
                    return addPoints(s.owner);
                }
            }
        }

        return 0;
    }
}
