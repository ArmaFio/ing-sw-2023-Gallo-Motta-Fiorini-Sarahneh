package it.polimi.ingsw.model.CommonGoalCards;

public class CommonGoalCard_9 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_9() {
        id = 9;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int lines;
        boolean cond;
        boolean[] present;

        lines = 0;
        present = new boolean[Game.N_COLORS];

        for (int i = 0; i < present.length; i++) {
            present[i] = false;
        }

        for (int i = 0; i < s.nCol; i++) {
            cond = true;
            for (int j = 0; j < s.nRow; j++) {
                if (present[s.getTile(j, i).color]) {
                    cond = false;
                }
                present[s.getTile(j, i).color] = true;
            }

            if (cond) {
                lines++;
            }
        }

        if (lines == 2) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
