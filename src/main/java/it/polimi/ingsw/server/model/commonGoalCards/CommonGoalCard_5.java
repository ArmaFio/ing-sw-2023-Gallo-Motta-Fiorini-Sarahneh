package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.TileType;
import it.polimi.ingsw.server.model.shelf.Shelf;

import java.util.Arrays;

public class CommonGoalCard_5 extends CommonGoalCard {

    public CommonGoalCard_5(int nPlayers) {
        super(nPlayers);
        id = 5;
        this.description = "Three columns each formed by 6 tiles " +
                "of maximum three different types. One " +
                "column can show the same or a different " +
                "combination of another column";
    }

    @Override
    public int check_objective(Shelf s) {
        int lines, cont;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_TYPES + 1];

        for (int i = 0; i < s.N_COLS; i++) {
            cont = 0;

            Arrays.fill(present, false);

            for (int j = 0; j < s.N_ROWS; j++) {
                if (!present[s.getTile(j, i).type.value()]) {
                    cont++;
                    present[s.getTile(j, i).type.value()] = true;
                }
            }

            if (cont <= 3 && !present[TileType.EMPTY.value()]) {
                lines++;
            }
        }

        if (lines >= 3) {
            return addPoints(s.owner);
        }
        return 0;
    }

}
