package it.polimi.ingsw.model;

/**
 * Enum with all the different type of tiles.
 * Changes on this class will affect the entire game.
 * It's possible to modify the names but the total number of names should not change, otherwise errors may occur.
 */
public enum TileType {
    CAT("Cat", "Green"),
    BOOK("Book", "White"),
    GAME("Game", "Orange"),
    FRAME("Frame", "Blue"),
    TROPHY("Trophy", "Light Blue"),
    PLANT("Plant", "Magenta"),
    NONE("None", "None");

    private final String name;
    private final String color;


    TileType(String name, String color) {
        this.name = name;
        this.color = color;
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