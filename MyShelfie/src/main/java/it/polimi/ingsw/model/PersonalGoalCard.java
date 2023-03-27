package it.polimi.ingsw.model

import it.polimi.ingsw.model.shelf.Shelf;
import it.polimi.ingsw.model.utils.LoadSave;

import java.io.Serializable;
import java.util.HashMap;

public class PersonalGoalCard {
    private final Tile.TileType[][] matrix; //TODO fai lista
    private int points;


    public PersonalGoalCard(int id) {
        CardSerialized cardsSer = (CardSerialized) LoadSave.read(Game.PERSONAL_GOALS_PATH);
        matrix = cardsSer.load_card(id);

        points = 0;
    }

    public int getPoints() {
        return points;
    }

    public int check_points(Shelf s) {
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
        private HashMap<String, int[]>[] dict;

        public CardSerialized(Tile.TileType[][] m) {

        }

        public CardSerialized(HashMap<String, int[]>[] dict) {
            this.dict = dict;
        }

        Tile.TileType[][] load_card(int id) {
            Tile.TileType[][] m = new Tile.TileType[Game.SHELF_ROWS][Game.SHELF_COLS];

            for (int i = 0; i < Game.SHELF_ROWS; i++) {
                for (int j = 0; j < Game.SHELF_COLS; j++) {
                    m[i][j] = Tile.TileType.NONE;
                }
            }

            if (id < 0 || id >= Game.N_PERSONAL_GOALS) {
                System.out.println("Index of PersonalGoalCard out of bound");
                return m;
            }

            for (String key : dict[id].keySet()) {
                m[dict[id].get(key)[0]][dict[id].get(key)[1]] = Tile.TileType.string_to_enum(key);
            }

            return m;
        }
    }
}
