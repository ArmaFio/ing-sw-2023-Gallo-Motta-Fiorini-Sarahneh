package it.polimi.ingsw.messages;

public enum ResponseType {
    NONE("None"),
    JOIN("Join"),
    CREATE("Create"),
    START("Start"),
    CURSOR("Cursor"),
    TILES("Tiles"),
    JOIN_FAILURE("JOIN_FAILURE"),
    JOIN_LOBBY("JOIN_LOBBY"),
    JOIN_SUCCESS("Join Success"),
    LOGIN_REQUEST("Login request"),
    LOGIN_RESPONSE("Login response"),
    @Deprecated
    LOGIN_OUTCOME("Login outcome"),
    LOGIN_SUCCESS("Login Success"),
    LOGIN_FAILURE("Login Failure"),
    STRING("String"),
    LOBBY_JOINED("Lobby Joined"),
    LOBBY_LIST("List of lobbies"),
    UPD_STATE("Update state"),
    LOBBY_DATA("Lobby Data");

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
