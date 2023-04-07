package it.polimi.ingsw;

import it.polimi.ingsw.response.ResponseType;

import java.io.Closeable;
import java.io.Serializable;

public abstract class Response implements Serializable, Closeable {
    public ResponseType type;
    protected User author;

    public Response(){

    }

    @Override
    public void close(){
        //TODO non toglierlo che da errore
    }

}
