package it.polimi.ingsw.response;

import it.polimi.ingsw.Response;
import it.polimi.ingsw.User;

public class JoinRequest extends Response {
    public JoinRequest(String s) {
        toObject(s);
    }

    @Deprecated
    private void toObject(String str) {
        boolean flag = true;
        int i, j;

        for (i = 0; i < str.length() && flag; i++) {
            if (str.charAt(i) == ':') {
                flag = false;
            }
        }

        for (j = 0, flag = true; j < str.length() && flag; j++) {
            if (str.charAt(j) == '}') {
                flag = false;
            }
        }

        this.author = new User(str.substring(i, j)); //TODO controlla che sia valido
    }

    @Override
    public String toString() {
        return "Join={" +
                "Author" + ':' + author.username +
                '}';
    }
}
