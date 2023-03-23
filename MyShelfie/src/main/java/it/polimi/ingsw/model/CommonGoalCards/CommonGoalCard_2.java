package it.polimi.ingsw.model.CommonGoalCards;

public class CommonGoalCard_2 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_2() {
        id = 2;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int[][] groups;
        int n_groups, cont;
        int[] dim_groups;

        groups = new int[s.nRow][s.nCol];

        dim_groups = find_groups_dim(s);

        cont = 0;
        for (int dim : dim_groups) {
            if (dim >= 4) {
                cont++;
            }
        }

        if (cont == 4) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }

    int find_groups(Shelf s, int[][] groups) {
        int n_groups;

        n_groups = 0;

        for (int i = 0; i < s.nRow; i++) {
            for (int j = 0; j < s.nCol; j++) {
                if (i - 1 >= 0 && s.getTile(i, j).color == s.getTile(i - 1, j).color) {
                    groups[i][j] = groups[i - 1][j];
                } else if (j - 1 > 0 && s.getTile(i, j).color == s.getTile(i, j - 1).color) {
                    groups[i][j] = groups[1][j - 1];
                } else {
                    n_groups++;
                    groups[i][j] = n_groups;
                }
            }
        }

        //TODO Controlla se corretto
        for (int i = 0; i < s.nRow; i++) {
            for (int j = 0; j < s.nCol; j++) {
                if (i - 1 >= 0 && groups[i][j] != groups[i - 1][j] && s.getTile(i, j).color == s.getTile(i - 1, j).color) {
                    groups[i][j] = groups[i - 1][j];
                } else if (j - 1 >= 0 && groups[i][j] != groups[i][j - 1] && s.getTile(i, j).color == s.getTile(i, j - 1).color) {
                    groups[i][j] = groups[i][j - 1];
                } else if (i + 1 < s.nRow && groups[i][j] != groups[i + 1][j] && s.getTile(i, j).color == s.getTile(i + 1, j).color) {
                    groups[i][j] = groups[i + 1][j];
                } else if (j + 1 < s.nCol && groups[i][j] != groups[i][j + 1] && s.getTile(i, j).color == s.getTile(i, j + 1).color) {
                    groups[i][j] = groups[i][j + 1];
                }
            }
        }

        return n_groups;
    }

    int[] find_groups_dim(Shelf s) {
        int[][] groups;
        int[] dim_groups;
        int n_groups;

        groups = new int[s.nRow][s.nCol];

        n_groups = find_groups(s, groups);

        dim_groups = new int[n_groups];

        for (int i = 0; i < dim_groups.length; i++) {
            dim_groups[i] = 0;
        }

        for (int i = 0; i < s.nRow; i++) {
            for (int j = 0; j < s.nCol; j++) {
                dim_groups[groups[i][j]]++;
            }
        }

        return dim_groups;
    }
}
