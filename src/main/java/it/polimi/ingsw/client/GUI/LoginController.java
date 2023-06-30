package it.polimi.ingsw.client.GUI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class LoginController {
    private final HashMap<Node, double[]> layoutRatio = new HashMap<>();
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
    private Label usernameText;
    @FXML
    private Label passwordText;
    @FXML
    private PasswordField password;
    private ViewGUI gui;
    private double initialWidth;
    private double initialHeight;


    /**
     * Gets the credentials given by the player.
     *
     * @throws IOException exception thrown if the method is unable to read the credentials.
     */
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

    /**
     * Handles the animation displayed while failing login.
     */
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

    /**
     * Handles the animation displayed while logging in successfully.
     */
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


    /**
     * Sets the reference to the view.
     *
     * @param gui the view.
     */
    public void setMainApp(ViewGUI gui) {
        this.gui = gui;
    }

    public void resizeWidth(double stageWidth, double initialWidth, double initialHeight) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        //if 0 -> not first time
        if (initialWidth == 0 && initialHeight == 0) {
            for (Node n : layoutRatio.keySet()) {
                n.setScaleX(stageWidth / this.initialWidth);
                n.setLayoutX(layoutRatio.get(n)[0] * stageWidth);
            }
        } else {
            this.initialWidth = initialWidth;
            this.initialHeight = initialHeight;
            double[] xy = new double[2];
            xy[0] = button.getLayoutX() / initialWidth;
            xy[1] = button.getLayoutY() / initialHeight;
            layoutRatio.put(button, xy);
            xy = new double[2];
            xy[0] = username.getLayoutX() / initialWidth;
            xy[1] = username.getLayoutY() / initialHeight;
            layoutRatio.put(username, xy);
            xy = new double[2];
            xy[0] = password.getLayoutX() / initialWidth;
            xy[1] = password.getLayoutY() / initialHeight;
            layoutRatio.put(password, xy);
            xy = new double[2];
            xy[0] = usernameText.getLayoutX() / initialWidth;
            xy[1] = usernameText.getLayoutY() / initialHeight;
            layoutRatio.put(usernameText, xy);
            xy = new double[2];
            xy[0] = passwordText.getLayoutX() / initialWidth;
            xy[1] = passwordText.getLayoutY() / initialHeight;
            layoutRatio.put(passwordText, xy);
            xy = new double[2];
            xy[0] = messageUser.getLayoutX() / initialWidth;
            xy[1] = messageUser.getLayoutY() / initialHeight;
            layoutRatio.put(messageUser, xy);
            for (Node n : layoutRatio.keySet()) {
                n.setScaleX(initialWidth / stageWidth);
                n.setLayoutX(layoutRatio.get(n)[0] * stageWidth);
            }
        }
    }


    public void resizeHeight(double stageHeight, double initialWidth, double initialHeight) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int height = gd.getDisplayMode().getHeight();
        //if 0 -> not first time
        if (initialWidth == 0 && initialHeight == 0) {
            for (Node n : layoutRatio.keySet()) {
                n.setScaleY(stageHeight / this.initialHeight);
                n.setLayoutY(layoutRatio.get(n)[1] * stageHeight);
            }
        } else {
            this.initialWidth = initialWidth;
            this.initialHeight = initialHeight;
            double[] xy = new double[2];
            xy[0] = button.getLayoutX() / initialWidth;
            xy[1] = button.getLayoutY() / initialHeight;
            layoutRatio.put(button, xy);
            xy = new double[2];
            xy[0] = username.getLayoutX() / initialWidth;
            xy[1] = username.getLayoutY() / initialHeight;
            layoutRatio.put(username, xy);
            xy = new double[2];
            xy[0] = password.getLayoutX() / initialWidth;
            xy[1] = password.getLayoutY() / initialHeight;
            layoutRatio.put(password, xy);
            xy = new double[2];
            xy[0] = usernameText.getLayoutX() / initialWidth;
            xy[1] = usernameText.getLayoutY() / initialHeight;
            layoutRatio.put(usernameText, xy);
            xy = new double[2];
            xy[0] = passwordText.getLayoutX() / initialWidth;
            xy[1] = passwordText.getLayoutY() / initialHeight;
            layoutRatio.put(passwordText, xy);
            xy = new double[2];
            xy[0] = messageUser.getLayoutX() / initialWidth;
            xy[1] = messageUser.getLayoutY() / initialHeight;
            for (Node n : layoutRatio.keySet()) {
                n.setScaleY(initialHeight / stageHeight);
                n.setLayoutY(layoutRatio.get(n)[1] * stageHeight);
            }
        }
    }
}

