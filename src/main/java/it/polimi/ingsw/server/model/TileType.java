package it.polimi.ingsw.server.model;

/**
 * Enum with all the different type of tiles.
 * Changes on this class will affect the entire game.
 * It's possible to modify the names but the total number of names should not change, otherwise errors may occur.
 */
public enum TileType {
    NONE,
    EMPTY,
    CAT,
    BOOK,
    GAME,
    FRAME,
    TROPHY,
    PLANT;

    public static TileType toEnum(int n) {
        for (TileType type : TileType.values()) {
            if (type.value() == n) {
                return type;
            }
        }

        System.out.println("Color doesn't exist: " + n);
        return TileType.EMPTY;
    }

    /**
     * Returns a tile given a color
     *
     * @param s the type of the tile
     * @return the tile's type corresponding to the indicated color
     * @author Gallo Matteo
     */
    public static TileType toEnum(String s) {
        for (TileType type : TileType.values()) {
            if (type.toString().equals(s)) {
                return type;
            }
        }

        System.out.println("Color doesn't exist");
        return TileType.EMPTY;
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

    /**
     * Tells if two tiles have the same type.
     *
     * @param other the other tile to compare.
     * @return true if the two tiles have the same type, false otherwise.
     */
    public boolean equals(TileType other) {
        return this.value() == other.value();
    }
}