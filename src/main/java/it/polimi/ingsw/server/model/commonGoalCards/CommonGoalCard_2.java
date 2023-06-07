package it.polimi.ingsw.server.model.commonGoalCards;

import it.polimi.ingsw.server.model.shelf.Shelf;

public class CommonGoalCard_2 extends CommonGoalCard {

    public CommonGoalCard_2(int nPlayers) {
        super(nPlayers);
        id = 2;
        this.description = "Four groups each containing at least " +
                "4 tiles of the same type (not necessarily " +
                "in the depicted shape). " +
                "The tiles of one group can be different " +
                "from those of another group.";
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
            return addPoints(s.owner);
        }
        return 0;
    }

}
