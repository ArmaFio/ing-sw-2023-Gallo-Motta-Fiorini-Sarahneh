package it.polimi.ingsw.client;

import it.polimi.ingsw.messages.CreateMessage;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.MessageType;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;

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
    private Label inLobbyLabel;
    @FXML
    private ListView<String> lobbyUsers;
    @FXML
    private Label startError;
    private String selectedCapacity;

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

    @FXML
    private void setCapacity() {
        selectedCapacity = lobbyCapacity.getValue();
    }


    public void onInvalidValue() {
        creationLabel.setText("Please select a valid number");
    }

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
    private void goBack() {
        try {
            gui.changeScene("/afterLogin.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onStart() {
        System.out.println("sending start message");
        Message response = new Message(MessageType.START);
        try {
            gui.write(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onEmptyLobby() {
        selectLabel.setText("Currently there are no lobbies available");
    }

    public void onNotEmptyLobby() {
        selectLabel.setText("Select a lobby");
        lobbyList.getItems().clear();
    }

    public void addLobby(int id, String admin, int capacity, int lobbyDim) {
        lobbyList.getItems().add("[" + id + "] " + admin + "'s lobby | " + capacity + "/" + lobbyDim);
    }

    public void setMainApp(ViewGUI gui) {
        this.gui = gui;
    }

    public int getSelection() {
        if (selectedCapacity != null) {
            return Integer.parseInt(selectedCapacity);
        } else {
            return 0;
        }
    }

    public void updateInsideLobby() {
        lobbyUsers.getItems().clear();
        for (String str : gui.getLobbyUsers()) {
            lobbyUsers.getItems().add(str);
        }
    }

    public void setLabel(String message) {
        startError.setText(message);
    }
}
