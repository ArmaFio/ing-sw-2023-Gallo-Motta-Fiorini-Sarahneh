package it.polimi.ingsw.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;


public class Controller {
    @FXML
    private TextField username;
    @FXML
    private Label messageUser;


    private boolean loginCheckB;
    @FXML
    public void userLogin() throws IOException {
        checkLogin();
    }

    @FXML
    private void checkLogin() throws IOException {
        loginCheckB = username.getText().toString().equals("khaled");
        if(loginCheckB){
            messageUser.setText("FUNZIONAAAA!!!!");
        }
    }


}

