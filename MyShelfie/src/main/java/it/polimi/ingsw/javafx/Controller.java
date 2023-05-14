package it.polimi.ingsw.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Controller {
    @FXML
    private TextField username;
    @FXML
    private Label messageUser;
    @FXML
    private PasswordField password;
    @FXML
    public Button button;
    public String[] credentials = new String[2];
    public final BlockingQueue<Object> queue = new LinkedBlockingQueue<>();

   @FXML
    private boolean loginCheckB;
    @FXML
    public void userLogin() throws IOException {
        checkLogin();
    }

    @FXML
    private void checkLogin() throws IOException {
        ViewGUI v = new ViewGUI();
        //String nome = username.getText().toString().toLowerCase();
        //String psw = password.getText().toString();
        //String[] credenziali = new String[2];
        //credenziali[0] = nome;
        //credenziali[1] = psw;
        loginCheckB = username.getText().toString().toLowerCase().equals("khaled")&& password.getText().toString().equals("123");
        if(loginCheckB){
            messageUser.setText("FUNZIONAAAA!!!!");

            v.changeScene("/afterLogin.fxml");
        }
        else{
            messageUser.setText("NON FUNZIONA");
        }
    }

    @FXML
    private void getCredentials() throws IOException{
        credentials[0] = username.getText();
        credentials[1] = password.getText();
        queue.offer(new Object());
    }


}

