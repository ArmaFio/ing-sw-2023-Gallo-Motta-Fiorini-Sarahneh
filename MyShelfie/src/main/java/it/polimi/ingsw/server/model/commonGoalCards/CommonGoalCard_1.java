package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.shelf.Shelf;

public class CommonGoalCard_1 extends CommonGoalCard {
    public CommonGoalCard_1() {
        id = 1;
    }

    @Override
    public int check_objective(Shelf s) {
        int cont;
        int[] dim_groups;

        dim_groups = s.find_groups();

        cont = 0;
        for (int dim : dim_groups) {
            if (dim >= 2) {
                cont++;
            }
        }

        if (cont >= 6) {
            return addPoints();
        }

        return 0;
    }
}



