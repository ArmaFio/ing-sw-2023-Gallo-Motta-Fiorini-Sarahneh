package it.polimi.ingsw.model;

import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.utils.LoadSave;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PersonalGoalCard {
    private final TileType[][] matrix; //TODO fai lista
    private int points;


    public PersonalGoalCard(ArrayList<Integer> personalObjs) {
        /*
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
        */

        Random random = new Random();
        Integer id = personalObjs.get(random.nextInt(personalObjs.size()));
        personalObjs.remove(id);

        CardSerialized cardsSer = (CardSerialized) LoadSave.read(Game.PERSONAL_GOALS_PATH);
        matrix = cardsSer.load_card(id);

        points = 0;
    }

    public int getPoints() {
        return points;
    }

    public int check_objective(Shelf s) {
        int cont = 0;

        for (int i = 0; i < s.N_ROWS; i++) {
            for (int j = 0; j < s.N_COLS; j++) {
                if (matrix[i][j] == s.getTile(i, j).type) {
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
                System.out.println("error: PersonalGoalCard");
                return 0;
            }
        }
    }


    private static class CardSerialized implements Serializable {
        private ArrayList<HashMap<String, int[]>> dict;

        public CardSerialized(TileType[][] m) {

        }

        public CardSerialized(ArrayList<HashMap<String, int[]>> dict) {
            this.dict = dict;
        }

        TileType[][] load_card(int id) {
            TileType[][] m = new TileType[Game.SHELF_ROWS][Game.SHELF_COLS];

            for (int i = 0; i < Game.SHELF_ROWS; i++) {
                for (int j = 0; j < Game.SHELF_COLS; j++) {
                    m[i][j] = TileType.NONE;
                }
            }

            if (id < 0 || id >= Game.N_PERSONAL_GOALS) {
                System.out.println("Index of PersonalGoalCard out of bound");
                return m;
            }

            for (String key : dict.get(id).keySet()) {
                m[dict.get(id).get(key)[0]][dict.get(id).get(key)[1]] = TileType.getEnum(key);
            }

            return m;
        }
    }
}
