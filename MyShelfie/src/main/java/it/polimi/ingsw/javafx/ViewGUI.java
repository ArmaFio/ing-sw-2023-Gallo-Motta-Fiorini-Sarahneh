package it.polimi.ingsw.javafx;

import it.polimi.ingsw.client.NetworkHandler;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewGUI extends Application {

    private static Stage state;
    @Override

    public void start(Stage stage) throws IOException {
        connect();
        state = stage;
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(ViewGUI.class.getResource("/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("MyShelfie Login");
        stage.setScene(scene);
        stage.show();
    }

    public void changeScene(String fxml) throws IOException{
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        state.getScene().setRoot(pane);


    }

    public void connect(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                new NetworkHandler(1);
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public static void main(String[] args){
        launch();
    }
}
