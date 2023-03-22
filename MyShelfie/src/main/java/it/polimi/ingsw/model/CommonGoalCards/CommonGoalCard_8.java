package it.polimi.ingsw.model.CommonGoalCards;

public class CommonGoalCard_8 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_8() {
        id = 8;
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

        for (int i = 0; i < s.nRow; i++) {
            cont = 0;
            for (int j = 0; j < s.nCol; j++) {
                if (present[s.getTile(i, j).color]) {
                    cont++;
                } else {
                    present[s.getTile(i, j).color] = true;
                }
            }

            if (cont <= 3) {
                lines++;
            }
        }

        if (lines == 4) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }
}
