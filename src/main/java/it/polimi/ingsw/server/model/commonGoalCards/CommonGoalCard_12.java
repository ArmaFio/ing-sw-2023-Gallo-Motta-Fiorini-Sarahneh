package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Shelf;

public class CommonGoalCard_12 extends CommonGoalCard {

    public CommonGoalCard_12(int nPlayers) {
        super(nPlayers);
        id = 12;
        this.description = "Five columns of increasing or decreasing " +
                "height. Starting from the first column on " +
                "the left or on the right, each next column " +
                "must be made of exactly one more tile. " +
                "Tiles can be of any type. ";
    }

    @Override
    public int check_objective(Shelf s) {
        int first;
        boolean increasing, decreasing;

        first = -1;

        for (int i = s.N_ROWS - 1; i >= 0; i--) {
            if (!s.getTile(i, 0).isEmpty()) {
                first = i;
            }
        }

        if (first == -1) {
            return 0;
        }

        increasing = decreasing = true;

        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                if ((j > i - first && !s.getTile(i, j).isEmpty()) ||
                        (j <= i - first && s.getTile(i, j).isEmpty())
                ) {
                    decreasing = false;
                }

                if ((j < first - i && !s.getTile(i, j).isEmpty()) ||
                        (j >= first - i && s.getTile(i, j).isEmpty())
                ) {
                    increasing = false;
                }
            }
        }

        if ((decreasing && (first == 0 || first == 1)) || (increasing && (first == 4 || first == 5))) {
            return addPoints(s.owner);
        }
        return 0;
    }
}
