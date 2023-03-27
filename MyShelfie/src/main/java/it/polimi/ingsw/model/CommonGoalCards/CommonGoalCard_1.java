package it.polimi.ingsw.model.CommonGoalCards;

import it.polimi.ingsw.model.shelf.Shelf;

public class CommonGoalCard_1 implements CommonGoalCard {
    private final int id;
    private int n_solved;

    public CommonGoalCard_1() {
        id = 1;
        n_solved = 0;
    }

    @Override
    public int check_objective(Shelf s) {
        int[][] groups;
        int n_groups, cont;
        int[] dim_groups;

        groups = new int[s.N_ROWS][s.N_COLS];

        dim_groups = find_groups_dim(s);

        cont = 0;
        for (int dim : dim_groups) {
            if (dim >= 2) {
                cont++;
            }
        }

        //TODO < 6 o == 6? Stessa domanda per altri common goals
        if (cont == 6) {
            n_solved++;
            return 8 - 2 * (n_solved - 1);
        }

        return 0;
    }

    /**
     * Given a Shelf returns a matrix containing the number of the group to which the tile in that position belongs
     *
     * @param s      A Shelf object
     * @param groups A matrix that will be modified
     * @return The number of groups
     * @author Gallo Matteo
     */
    private int find_groups(Shelf s, int[][] groups) {
        int n_groups;

        n_groups = 0;

        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                if (i - 1 >= 0 && s.getTile(i, j).type == s.getTile(i - 1, j).type) {
                    groups[i][j] = groups[i - 1][j];
                } else if (j - 1 > 0 && s.getTile(i, j).type == s.getTile(i, j - 1).type) {
                    groups[i][j] = groups[1][j - 1];
                } else {
                    n_groups++;
                    groups[i][j] = n_groups;
                }
            }
        }

        //TODO Controlla se corretto
        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                if (i - 1 >= 0 && groups[i][j] != groups[i - 1][j] && s.getTile(i, j).type == s.getTile(i - 1, j).type) {
                    groups[i][j] = groups[i - 1][j];
                } else if (j - 1 >= 0 && groups[i][j] != groups[i][j - 1] && s.getTile(i, j).type == s.getTile(i, j - 1).type) {
                    groups[i][j] = groups[i][j - 1];
                } else if (i + 1 < s.N_ROWS && groups[i][j] != groups[i + 1][j] && s.getTile(i, j).type == s.getTile(i + 1, j).type) {
                    groups[i][j] = groups[i + 1][j];
                } else if (j + 1 < s.N_COLS && groups[i][j] != groups[i][j + 1] && s.getTile(i, j).type == s.getTile(i, j + 1).type) {
                    groups[i][j] = groups[i][j + 1];
                }
            }
        }

        return n_groups;
    }

    /**
     * Given a Shelf returns the dimension of each group in it
     *
     * @param s A Shelf object
     * @return An array of integer containing the dimensions of each group contained in Shelf
     * @author Gallo Matteo
     */
    private int[] find_groups_dim(Shelf s) {
        int[][] groups;
        int[] dim_groups;
        int n_groups;

        groups = new int[s.N_ROWS][s.N_COLS];

        n_groups = find_groups(s, groups);

        dim_groups = new int[n_groups];

        for (int i = 0; i < dim_groups.length; i++) {
            dim_groups[i] = 0;
        }

        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                dim_groups[groups[i][j]]++;
            }
        }

        return dim_groups;
    }
}
