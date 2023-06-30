package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.tiles.TileType;
import it.polimi.ingsw.utils.LoadSave;
import it.polimi.ingsw.utils.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class PersonalGoalCard {
    public final int id;
    private final TileType[][] matrix;


    public PersonalGoalCard(int id) {

        ArrayList<HashMap<String, int[]>> dicts = new ArrayList<>(12);
        dicts.add(PersonalGoalCardsList.myMap_1);
        dicts.add(PersonalGoalCardsList.myMap_2);
        dicts.add(PersonalGoalCardsList.myMap_3);
        dicts.add(PersonalGoalCardsList.myMap_4);
        dicts.add(PersonalGoalCardsList.myMap_5);
        dicts.add(PersonalGoalCardsList.myMap_6);
        dicts.add(PersonalGoalCardsList.myMap_7);
        dicts.add(PersonalGoalCardsList.myMap_8);
        dicts.add(PersonalGoalCardsList.myMap_9);
        dicts.add(PersonalGoalCardsList.myMap_10);
        dicts.add(PersonalGoalCardsList.myMap_11);
        dicts.add(PersonalGoalCardsList.myMap_12);
        CardSerialized cards = new CardSerialized(dicts);
        LoadSave.write(Game.PERSONAL_GOALS_PATH, cards);


        this.id = id;

        CardSerialized cardsSer = (CardSerialized) LoadSave.read(Game.PERSONAL_GOALS_PATH);
        this.matrix = cardsSer.load_card(this.id);
    }


    /**
     * Draws {@code n} personal goal cards not duplicated.
     *
     * @param n the number of {@code PersonalGoalCard} to draw.
     * @return ids of the drawn {@code PersonalGoalCard}.
     */
    public static int[] draw(int n) {
        boolean found;
        int drawn;
        int count = 0;
        int[] ids = new int[n];
        Random randomGenerator = new Random();

        for (int i = 0; i < n; i++) {
            do {
                drawn = randomGenerator.nextInt(Game.N_PERSONAL_GOALS);
                Logger.debug("extracted for " + n + " players: " + drawn);
                found = true;
                for (int j = 0; j < count; j++) {
                    if (ids[j] == drawn) {
                        found = false;
                        break;
                    }
                }
            } while (!found);

            ids[i] = drawn;
            count++;
        }

        return ids;
    }


    /**
     * Checks the completion status of the {@code PersonalGoalCard}.
     *
     * @param s the shelf of the player.
     * @return the number of point assigned.
     */
    public int checkObjective(Shelf s) {
        int cont = 0;

        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                if (matrix[i][j] != TileType.EMPTY && matrix[i][j].equals(s.getMatrix()[i][j].type)) {
                    cont++;
                }
            }
        }

        switch (cont) {
            case 0 -> {
                return 0;
            }
            case 1 -> {
                return 1;
            }
            case 2 -> {
                return 2;
            }
            case 3 -> {
                return 4;
            }
            case 4 -> {
                return 6;
            }
            case 5 -> {
                return 9;
            }
            case 6 -> {
                return 12;
            }
            default -> {
                System.out.println("error: PersonalGoalCard checkObjective");
                return 0;
            }
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(matrix);
    }

    private static class CardSerialized implements Serializable {
        private final ArrayList<HashMap<String, int[]>> dict;

        public CardSerialized(ArrayList<HashMap<String, int[]>> dict) {
            this.dict = dict;
        }

        TileType[][] load_card(int id) {
            TileType[][] m = new TileType[Game.SHELF_ROWS][Game.SHELF_COLS];

            for (int i = 0; i < Game.SHELF_ROWS; i++) {
                for (int j = 0; j < Game.SHELF_COLS; j++) {
                    m[i][j] = TileType.EMPTY;
                }
            }

            if (id < 0 || id >= Game.N_PERSONAL_GOALS) {
                System.out.println("Index of PersonalGoalCard out of bound");
                return m;
            }

            for (String key : dict.get(id).keySet()) {
                m[dict.get(id).get(key)[0]][dict.get(id).get(key)[1]] = TileType.toEnum(key);
            }

            return m;
        }
    }

    public TileType[][] getMatrix() {
        return matrix;
    }
}
