package it.polimi.ingsw.model;

public class Tile {
    public final int id;
    public final TileType type;

    public Tile(TileType type) {
        this(0, type);
    }

    public Tile(int id, TileType type) {
        this.id = id;
        this.type = type;
    }

    /**
     * The 6 types of {@code Tile} in the game
     */
    public enum TileType {
        CAT("Cat", "Green", new int[]{0, 0, 0}),
        BOOK("Book", "White", new int[]{0, 0, 0}),
        GAME("Game", "Orange", new int[]{0, 0, 0}),
        FRAME("Frame", "Blue", new int[]{0, 0, 0}),
        TROPHY("Trophy", "Light Blue", new int[]{0, 0, 0}),
        PLANT("Plant", "Magenta", new int[]{0, 0, 0}),
        NONE("None", "None", new int[]{0, 0, 0});

        private final String name;
        private final Color color;


        TileType(String name, String color, int[] rgb) {
            this.name = name;
            this.color = new Color(color, rgb);
        }

        /**
         * Returns a tile given a color
         *
         * @param s the color of the tile
         * @return the tile's type corresponding to the indicated color
         * @author Gallo Matteo
         */
        public static TileType string_to_enum(String s) {
            for (TileType type : TileType.values()) {
                if (type.toString().equals(s)) {
                    return type;
                }
            }

            System.out.println("Color doesn't exist");
            return TileType.NONE;
        }

        /**
         * Gives the index of the tile's type
         *
         * @return The index of the tile's type
         * @author Gallo Matteo
         */
        public int value() {
            return this.ordinal();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Represents a color
     */
    private static class Color {
        public final int r, g, b;
        private final String name;


        Color(String name, int[] rgb) {
            this.name = name;

            if (rgb[0] >= 0 && rgb[0] <= 255) {
                this.r = rgb[0];
            } else {
                r = 0;
                System.out.print("Color not compatible");
            }

            if (rgb[1] >= 0 && rgb[1] <= 255) {
                g = rgb[1];
            } else {
                g = 0;
                System.out.print("Color not compatible");
            }

            if (rgb[2] >= 0 && rgb[2] <= 255) {
                b = rgb[2];
            } else {
                b = 0;
                System.out.print("Color not compatible");
            }
        }


        @Override
        public String toString() {
            return name;
        }
    }
}
