package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.server.model.shelf.Shelf;

public class CommonGoalCard_9 extends CommonGoalCard {

    public CommonGoalCard_9(int nPlayers) {
        super(nPlayers);
        id = 9;
        this.description = "Two columns each formed by 6 " +
                "different types of tiles. ";
    }

    @Override
    public int check_objective(Shelf s) {
        int lines;
        boolean cond;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_TYPES + 1];

        for (int i = 0; i < s.N_COLS; i++) {
            cond = true;

            for (int j = 0; j < present.length; j++) {
                present[j] = false;
            }

            for (int j = 0; j < s.N_ROWS; j++) {
                if (present[s.getTile(j, i).type.value()]) {
                    cond = false;
                }
                present[s.getTile(j, i).type.value()] = true;
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
