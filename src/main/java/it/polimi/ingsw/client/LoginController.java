package it.polimi.ingsw.client;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class LoginController {
    @FXML
    public Button button;
    public String[] credentials = new String[2];
    public BlockingQueue<Object> queue = new LinkedBlockingQueue<>();
    public boolean available = false;
    @FXML
    private TextField username;
    @FXML
    private Label messageUser;
    @FXML
    private PasswordField password;
    private ViewGUI gui;

    @FXML
    private void getCredentials() throws IOException {
        credentials[0] = username.getText();
        credentials[1] = password.getText();
        if (credentials[0] != null && credentials[1] != null && !credentials[0].equals("") && !credentials[1].equals("")) {
            queue.offer(new Object());
        } else {
            onLoginFailure();
        }
        //available = true;
    }

    public synchronized void onLoginFailure() {
        messageUser.setText("INVALID CREDENTIALS");
        double x0 = messageUser.getTranslateX();
        double y0 = messageUser.getTranslateY();
        double shakeDistance = 10;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> messageUser.setVisible(true), new KeyValue(messageUser.translateXProperty(), x0)),
                new KeyFrame(Duration.millis(100), new KeyValue(messageUser.translateXProperty(), x0 - shakeDistance)),
                new KeyFrame(Duration.millis(200), new KeyValue(messageUser.translateXProperty(), x0 + shakeDistance)),
                new KeyFrame(Duration.millis(300), new KeyValue(messageUser.translateXProperty(), x0 - shakeDistance)),
                new KeyFrame(Duration.millis(400), new KeyValue(messageUser.translateXProperty(), x0 + shakeDistance)),
                new KeyFrame(Duration.millis(500), new KeyValue(messageUser.translateXProperty(), x0)),
                new KeyFrame(Duration.ZERO, new KeyValue(messageUser.translateYProperty(), y0)),
                new KeyFrame(Duration.millis(100), new KeyValue(messageUser.translateYProperty(), y0 - shakeDistance)),
                new KeyFrame(Duration.millis(200), new KeyValue(messageUser.translateYProperty(), y0 + shakeDistance)),
                new KeyFrame(Duration.millis(300), new KeyValue(messageUser.translateYProperty(), y0 - shakeDistance)),
                new KeyFrame(Duration.millis(400), new KeyValue(messageUser.translateYProperty(), y0 + shakeDistance)),
                new KeyFrame(Duration.millis(500), new KeyValue(messageUser.translateYProperty(), y0)),
                new KeyFrame(Duration.seconds(1), event -> messageUser.setVisible(false))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    public synchronized void onLoginSuccess() {
        TranslateTransition jumpAnimation = new TranslateTransition(Duration.seconds(0.2), messageUser);
        jumpAnimation.setFromY(0);
        jumpAnimation.setToY(-10);
        jumpAnimation.setAutoReverse(true);
        jumpAnimation.setCycleCount(2);
        messageUser.setVisible(true);
        messageUser.setText("LOGGING IN");
        messageUser.setTextFill(Color.GREEN);
        jumpAnimation.play();
    }


    public void setMainApp(ViewGUI gui) {
        this.gui = gui;
    }
}

