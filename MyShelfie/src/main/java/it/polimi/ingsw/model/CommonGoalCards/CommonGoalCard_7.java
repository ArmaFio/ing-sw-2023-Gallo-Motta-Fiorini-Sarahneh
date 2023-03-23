package it.polimi.ingsw.model.CommonGoalCards;

public class CommonGoalCard_7 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_7() {
        id = 7;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        boolean diag_a, diag_b, diag_c, diag_d;

        diag_a = diag_b = diag_c = diag_d = true;

        for (int i = 0; i < s.nRow; i++) {
            if (s.getTile(0, 0).color != s.getTile(i, i).color) {
                diag_a = false;
            }
            if (s.getTile(0, 1).color != s.getTile(i, i + 1).color) {
                diag_b = false;
            }
            if (s.getTile(0, s.nCol).color != s.getTile(i, s.Col - i).color) {
                diag_c = false;
            }
            if (s.getTile(0, s.nCol + 1).color != s.getTile(i + 1, s.Col - i).color) {
                diag_d = false;
            }
        }

        if (diag_a || diag_b || diag_c || diag_d) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
