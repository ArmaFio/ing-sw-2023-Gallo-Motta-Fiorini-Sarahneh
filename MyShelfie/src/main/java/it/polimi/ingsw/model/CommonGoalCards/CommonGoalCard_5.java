package it.polimi.ingsw.model.CommonGoalCards;
import it.polimi.ingsw.model.Shelf;
public class CommonGoalCard_5 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_5() {
        id = 5;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int lines, cont;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_COLORS];

        for (int i = 0; i < present.length; i++) {
            present[i] = false;
        }

        for (int i = 0; i < s.nCol; i++) {
            cont = 0;
            for (int j = 0; j < s.nRow; j++) {
                if (present[s.getTile(j, i).color]) {
                    cont++;
                } else {
                    present[s.getTile(j, i).color] = true;
                }
            }

            if (cont <= 3) {
                lines++;
            }
        }

        if (lines == 3) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
