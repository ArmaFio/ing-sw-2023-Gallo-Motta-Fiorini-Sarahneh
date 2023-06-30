package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Shelf;

public class CommonGoalCard_3 extends CommonGoalCard {
    public CommonGoalCard_3(int nPlayers) {
        super(nPlayers);
        id = 3;
        this.description = "Four tiles of the same type in the four " +
                "corners of the bookshelf. ";
    }

    @Override
    public int check_objective(Shelf s) {
        if (s.getTile(0, 0).equalsType(s.getTile(0, s.N_COLS - 1)) &&
                s.getTile(0, 0).equalsType(s.getTile(s.N_ROWS - 1, s.N_COLS - 1)) &&
                s.getTile(0, 0).equalsType(s.getTile(s.N_ROWS - 1, 0)) &&
                !s.getTile(0, 0).isEmpty()
        ) {
            return addPoints(s.owner);
        }

        return 0;
    }
}
