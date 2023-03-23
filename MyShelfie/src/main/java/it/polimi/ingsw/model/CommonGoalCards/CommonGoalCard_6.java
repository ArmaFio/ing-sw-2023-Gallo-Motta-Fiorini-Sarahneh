package it.polimi.ingsw.model.CommonGoalCards;

public class CommonGoalCard_6 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_6() {
        id = 5;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int[] equal;

        equal = new int[Game.N_COLORS];

        for (int i = 0; i < s.nRow; i++) {
            for (int j = 0; j < s.nCol; j++) {
                equal[s.getTile(i, j).color]++;
            }
        }

        for (int e : equal) {
            if (e >= 8) {
                n_solved++;
                return 8 - 2 * (n_solved - 1);
            }
        }

        return 0;
    }
}
