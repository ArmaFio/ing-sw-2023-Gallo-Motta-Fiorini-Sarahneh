package it.polimi.ingsw.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewGUI extends Application {

    private static Stage stato;
    @Override

    public void start(Stage stage) throws IOException {
        stato = stage;
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(ViewGUI.class.getResource("/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("MyShelfie Login");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stato.getScene().setRoot(pane);


    }

    public static void main(String[] args){
        launch();
    }
}
