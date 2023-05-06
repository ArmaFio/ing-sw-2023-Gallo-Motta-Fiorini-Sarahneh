package it.polimi.ingsw.messages;

public enum MessageType {
    NONE,
    STATE_UPD,
    LOGIN_REQUEST,
    LOGIN_RESPONSE,
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    @Deprecated
    LOGIN_OUTCOME,
    JOIN,
    LOBBIES_LIST,
    JOIN_LOBBY,
    CREATE,
    JOIN_SUCCEED,
    JOIN_FAILURE,
    LOBBY_DATA,
    START,
    GAME_UPD,
    @Deprecated
    TILES,
    TILES_REQUEST,
    TILES_RESPONSE,
    COLUMN_RESPONSE,
    COLUMN_REQUEST,
    @Deprecated
    STRING,
    CURSOR
}
