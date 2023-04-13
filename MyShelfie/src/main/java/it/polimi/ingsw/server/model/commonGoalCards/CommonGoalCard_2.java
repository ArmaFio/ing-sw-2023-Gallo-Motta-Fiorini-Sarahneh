package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.shelf.Shelf;

public class CommonGoalCard_2 extends CommonGoalCard {
    public CommonGoalCard_2() {
        id = 2;
    }

    @Override
    public int check_objective(Shelf s) {
        int cont;
        int[] dim_groups;

        dim_groups = s.find_groups();

        cont = 0;
        for (int dim : dim_groups) {
            if (dim >= 4) {
                cont++;
            }
        }

        if (cont >= 4) {
            return addPoints();
        }

        return 0;
    }
}
