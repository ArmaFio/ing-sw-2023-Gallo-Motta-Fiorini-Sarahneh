package it.polimi.ingsw.messages;

public enum ResponseType {
    NONE("None"),
    JOIN("Join"),
    CREATE("cre"),
    START("Start"),
    CURSOR("Cursor"),
    TILES("Tiles"),
    JOIN_FAILURE(""),
    JOIN_LOBBY(""),
    JOIN_SUCCESS(""),
    LOGIN_REQUEST("Login request"),
    LOGIN_RESPONSE("Login response"),
    @Deprecated
    LOGIN_OUTCOME("Login outcome"),
    LOGIN_SUCCESS(""),
    LOGIN_FAILURE(""),
    STRING("String"),
    LOBBY_JOINED(""),
    LOBBY_LIST("");

    //TODO probabilmente rimuovere da qui in avanti
    private final String name;

    ResponseType(String name) {
        this.name = name;
    }

    @Deprecated
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
