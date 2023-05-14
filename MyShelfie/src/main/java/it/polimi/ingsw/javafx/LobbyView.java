package it.polimi.ingsw.javafx;

import javafx.fxml.FXML;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class LobbyView {
    @FXML
    private Button JoinLobby;
    @FXML
    private Button CreateLobby;

    public void joinlobby(ActionEvent event) throws IOException{
        nextscene();

    }

    public void createlobby(ActionEvent event) throws IOException{
        nextscene();


    }

    public void nextscene() throws IOException{
        ViewGUI v =new ViewGUI();

        v.changeScene("/main-view.fxml");


    }
}
