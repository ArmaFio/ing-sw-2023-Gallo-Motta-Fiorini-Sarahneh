package it.polimi.ingsw.model.CommonGoalCards;

public class CommonGoalCard_11 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_11() {
        id = 11;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {

        for (int i = 1; i < s.nRow - 1; i++) {
            for (int j = 1; j < s.nCol - 1; j++) {
                if (s.getTile(i, j).color == s.getTile(i - 1, j - 1) &&
                        s.getTile(i, j).color == s.getTile(i + 1, j + 1) &&
                        s.getTile(i, j).color == s.getTile(i - 1, j + 1) &&
                        s.getTile(i, j).color == s.getTile(i + 1, j - 1)
                ) {
                    n_solved++;
                    return 8 - 2 * (n_solved - 1);
                }
            }
        }

        return 0;
    }
}
