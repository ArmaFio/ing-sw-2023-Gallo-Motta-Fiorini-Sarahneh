package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.shelf.Shelf;

public class CommonGoalCard_4 extends CommonGoalCard {

    public CommonGoalCard_4(int nPlayers) {
        super(nPlayers);
        id = 4;
        this.description = "Two groups each containing 4 tiles of " +
                "the same type in a 2x2 square. The tiles " +
                "of one square can be different from " +
                "those of the other square.";
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
                        if (s.getTile(i, j).isEmpty() || !s.getTile(i, j).equalsType(s.getTile(i + k, j + m))) {
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
            return addPoints(s.owner);
        }

        return 0;
    }
}
