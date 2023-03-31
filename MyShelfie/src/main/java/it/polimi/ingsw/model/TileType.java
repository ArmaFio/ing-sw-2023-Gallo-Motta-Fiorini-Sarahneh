package it.polimi.ingsw.model;

/**
 * Enum with all the different type of tiles.
 * Changes on this class will affect the entire game.
 * It's possible to modify the names but the total number of names should not change, otherwise errors may occur.
 */
public enum TileType {
    NONE("None", "None"),
    CAT("Cat", "Green"),
    BOOK("Book", "White"),
    GAME("Game", "Orange"),
    FRAME("Frame", "Blue"),
    TROPHY("Trophy", "Light Blue"),
    PLANT("Plant", "Magenta");


    private final String name;
    private final String color;


    TileType(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static TileType getEnum(int n) {
        for (TileType type : TileType.values()) {
            if (type.value() == n) {
                return type;
            }
        }

        System.out.println("Color doesn't exist");
        return TileType.NONE;
    }

    /**
     * Returns a tile given a color
     *
     * @param s the type of the tile
     * @return the tile's type corresponding to the indicated color
     * @author Gallo Matteo
     */
    public static TileType getEnum(String s) {
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

    public boolean equals(TileType other) {
        return this.value() == other.value();
    }

    public boolean isNone(){
        return this.equals(TileType.NONE);
    }
}