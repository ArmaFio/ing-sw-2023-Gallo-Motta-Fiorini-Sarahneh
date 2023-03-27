package it.polimi.ingsw.model.CommonGoalCards;
import it.polimi.ingsw.model.shelf.Shelf;
public class CommonGoalCard_12 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_12() {
        id = 12;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int first;
        boolean increasing, decreasing;

        first = -1;

        for (int i = s.N_ROWS - 1; i >= 0; i--) {
            if (s.getTile(i, 0) != null) {
                first = i;
            }
        }

        if (first == -1) {
            return 0;
        }

        increasing = decreasing = true;

        for (int i = 1; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                if ((j < first + i && s.getTile(i, j) != null) ||
                        (j >= first + i && s.getTile(i, j) == null)
                ) {
                    decreasing = false;
                }

                if ((j < first - i && s.getTile(i, j) != null) ||
                        (j >= first - i && s.getTile(i, j) == null)
                ) {
                    increasing = false;
                }
            }
        }

        if (increasing || decreasing) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
