package it.polimi.ingsw.client;

import it.polimi.ingsw.GameState;
import it.polimi.ingsw.messages.CreateMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import it.polimi.ingsw.messages.StringMessage;
import it.polimi.ingsw.utils.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateJoinController {

    private ViewGUI gui;
    @FXML
    private Button create;
    @FXML
    private Button join;
    private final String[] capacities = {"2", "3", "4"};
    @FXML
    private Button back;
    @FXML
    private Button selectCapacity;
    @FXML
    private Button joinSelectedLobby;
    @FXML
    private ListView<String> lobbyList;
    @FXML
    private ComboBox<String> lobbyCapacity;
    @FXML
    private Label creationLabel;
    @FXML
    private Label selectLabel;
    @FXML
    private Button startButton;
    @FXML
    private Button backButton;
    @FXML
    private Label inLobbyLabel;
    @FXML
    private ListView<String> lobbyUsers = new ListView<>();
    @FXML
    private Label startError;
    @FXML
    private VBox chatBox;
    @FXML
    private TextField chatBar;
    private String selectedCapacity;
    private String selectedLobby;


    /**
     * Function called by a button, begins the lobby creation procedure.
     */
    @FXML
    private void createLobby() {
        try {
            gui.changeScene("/LobbyCreation.fxml");
            lobbyCapacity.getItems().addAll(capacities);
            new Thread() {
                @Override
                public void run() {
                    try {
                        Message response = new CreateMessage(gui.askLobbyDim());
                        gui.write(response);
                        gui.changeScene("/InLobby.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sets the max capacity of the lobby.
     */
    @FXML
    private void setCapacity() {
        selectedCapacity = lobbyCapacity.getValue();
    }


    public void onInvalidValue() {
        creationLabel.setText("Please select a valid number");
    }


    /**
     * Function called by a button, allows to enter the LobbyList scene.
     */
    @FXML
    private void joinLobby() {
        //System.out.println("join");
        try {
            gui.changeScene("/LobbyList.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message response = new Message(MessageType.JOIN);
        try {
            gui.write(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackFromLobby() {
        try {
            gui.updateState(GameState.CREATE_JOIN);
            Message response = new Message(MessageType.EXIT_LOBBY);
            gui.write(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * function called by a button, allows to go back to the afterLogin scene.
     */
    @FXML
    private void goBack() {
        try {
            gui.updateState(GameState.CREATE_JOIN);
            gui.changeScene("/afterLogin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * When pressing the start button, send a start message to the server.
     */
    @FXML
    private void onStart() {
        Message response = new Message(MessageType.START);
        try {
            gui.write(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void selectLobby() {
        selectedLobby = lobbyList.getSelectionModel().getSelectedItem();
        System.out.println(selectedLobby);
    }

    @FXML
    private void joinSelectedLobby() {
        if (selectedLobby != null) {
            String selected = "";
            Matcher m = Pattern.compile("\\[(.*?)\\]").matcher(selectedLobby);
            while (m.find()) {
                selected = m.group(1);
            }
            Message response = new Message(MessageType.JOIN_LOBBY, Integer.parseInt(selected));
            try {
                gui.write(response); //TODO cambia
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                gui.changeScene("/InLobby.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void writeOnChat() {
        if (!chatBar.getText().isBlank()) {
            Label message = new Label("[" + gui.getUsername() + "] " + chatBar.getText());
            message.setAlignment(Pos.CENTER_RIGHT);
            chatBox.getChildren().add(message);
            StringMessage response = new StringMessage(chatBar.getText());
            try {
                gui.write(response);
            } catch (IOException e) {
                Logger.error("Unable to send chat message");
            }
            chatBar.clear();
        } else {
            chatBar.clear();
        }
    }

    public void onEmptyLobby() {
        selectLabel.setText("Currently there are no lobbies available");
        lobbyList.getItems().clear();
    }

    public void onNotEmptyLobby() {
        selectLabel.setText("Select a lobby");
        lobbyList.getItems().clear();
    }

    /**
     * Adds the lobby to the {@code ListView<>} of the available lobbies.
     *
     * @param id       id of the lobby.
     * @param admin    lobby's admin.
     * @param capacity lobby's capacity.
     * @param lobbyDim max players allowed in the lobby.
     */
    public void addLobby(int id, String admin, int capacity, int lobbyDim) {
        lobbyList.getItems().add("[" + id + "] " + admin + "'s lobby | " + capacity + "/" + lobbyDim);
    }

    public void setMainApp(ViewGUI gui) {
        this.gui = gui;
    }

    /**
     * Gets the selected lobby dimension.
     *
     * @return the lobby dimension chosen.
     */
    public int getSelection() {
        if (selectedCapacity != null) {
            return Integer.parseInt(selectedCapacity);
        } else {
            return 0;
        }
    }

    /**
     * Updates the list of users inside the lobby.
     */
    public void updateInsideLobby() {
        lobbyUsers.getItems().clear();
        for (String str : gui.getLobbyUsers()) {
            lobbyUsers.getItems().add(str);
        }
    }

    /**
     * Sets the label in the lobby.
     *
     * @param message the message to be displayed.
     */
    public void setLabel(String message) {
        startError.setText(message);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> startError.setVisible(true)),
                new KeyFrame(Duration.seconds(2), event -> startError.setVisible(false))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    public void updateChat(String[][] chat) {
        chatBox.getChildren().clear();
        for (int i = 0; i < chat.length; i++) {
            Label message = new Label("[" + chat[i][0] + "] " + chat[i][1]);
            message.setMaxWidth(chatBox.getMaxWidth());
            message.setStyle("-fx-background-color:purple;" +
                    "  -fx-text-fill:white;" +
                    " -fx-pref-height:20px;" +
                    "  -fx-pref-width:221px; ");
            if (chat[i][0].equals(gui.getUsername())) {
                message.setAlignment(Pos.CENTER_RIGHT);
            } else {
                message.setAlignment(Pos.CENTER_LEFT);
            }
            chatBox.getChildren().add(message);
        }
    }
}
