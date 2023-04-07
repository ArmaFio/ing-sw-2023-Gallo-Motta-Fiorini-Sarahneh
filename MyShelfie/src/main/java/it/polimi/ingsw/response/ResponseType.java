package it.polimi.ingsw.response;

public enum ResponseType {
    NONE("None"),
    JOIN("Join"),
    START("Start"),
    CURSOR("Cursor"),
    TILES("Tiles"),
    LOGIN_REQUEST("Login request"),
    STRING("String");

    //TODO probabilmente rimuovere da qui in avanti
    private final String name;

    ResponseType(String name) {
        this.name = name;
    }

    public static ResponseType toEnum(String str) {
        for (ResponseType type : ResponseType.values()) {
            if (type.toString().equals(str)) {
                return type;
            }
        }

        System.out.println("Response doesn't exist");
        return ResponseType.NONE;
    }

    @Override
    public String toString() {
        return name;
    }
}
