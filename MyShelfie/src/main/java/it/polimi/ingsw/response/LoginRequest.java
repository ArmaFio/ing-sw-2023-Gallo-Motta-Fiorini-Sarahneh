package it.polimi.ingsw.response;

import it.polimi.ingsw.Response;

public class LoginRequest extends Response {
    public LoginRequest(){
        type = ResponseType.LOGIN_REQUEST;
        author = null;
    }
}
