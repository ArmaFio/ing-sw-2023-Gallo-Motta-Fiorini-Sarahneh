package it.polimi.ingsw.messages;

public enum MessageType {
    NONE,
    JOIN,
    CREATE,
    START,
    CURSOR,
    @Deprecated
    TILES,
    JOIN_FAILURE,
    JOIN_LOBBY,
    JOIN_SUCCESS,
    LOGIN_REQUEST,
    LOGIN_RESPONSE,
    @Deprecated
    LOGIN_OUTCOME,
    LOGIN_SUCCESS,
    LOGIN_FAILURE,
    STRING,
    LOBBY_JOINED,
    LOBBY_LIST,
    UPD_STATE,
    LOBBY_DATA,
    UPDATE_GAME, TILES_RESPONSE, COLUMN_RESPONSE, COLUMN_REQUEST, TILES_REQUEST
}
