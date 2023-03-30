package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_12 extends CommonGoalCard {
    public CommonGoalCard_12() {
        id = 12;
    }

    @Override
    public int check_objective(Shelf s) {
        int first;
        boolean increasing, decreasing;

        first = -1;

        for (int i = s.N_ROWS - 1; i >= 0; i--) {
            if (!s.getTile(i, 0).type.equals(TileType.NONE)) {
                first = i;
            }
        }

        if (first == -1) {
            return 0;
        }

        increasing = decreasing = true;

        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                if ((j > i - first && !s.getTile(i, j).type.equals(TileType.NONE)) ||
                        (j <= i - first && s.getTile(i, j).type.equals(TileType.NONE))
                ) {
                    decreasing = false;
                }

                if ((j < first - i && !s.getTile(i, j).type.equals(TileType.NONE)) ||
                        (j >= first - i && s.getTile(i, j).type.equals(TileType.NONE))
                ) {
                    increasing = false;
                }
            }
        }

        if ((decreasing && (first == 0 || first == 1)) || (increasing && (first == 4 || first == 5))) {
            return addPoints();
        }

        return 0;
    }
}
