package it.polimi.ingsw.model.CommonGoalCards;
import it.polimi.ingsw.model.Shelf;
public class CommonGoalCard_3 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_3() {
        id = 3;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        if (s.getTile(0, 0).color == s.getTile(0, s.nCol - 1).color &&
                s.getTile(0, 0).color == s.getTile(s.nRow - 1, s.nCol - 1).color &&
                s.getTile(0, 0).color == s.getTile(s.nRow - 1, 0).color
        ) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
