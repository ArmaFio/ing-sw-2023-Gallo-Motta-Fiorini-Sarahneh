package it.polimi.ingsw;

import it.polimi.ingsw.response.ResponseType;

import java.io.Serializable;

public abstract class Response implements Serializable {
    public ResponseType type;
    protected User author;

}
