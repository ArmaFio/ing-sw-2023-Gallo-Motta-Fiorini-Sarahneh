package it.polimi.ingsw.javafx;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class LabelUtils {
    private static Label labelToUpdate;

    public static void setLabelToUpdate(Label label) {
        labelToUpdate = label;
    }

    public static void updateLabelLogin(String text) {
        if (labelToUpdate != null) {
            Platform.runLater(() -> labelToUpdate.setText(text));
        }
    }
}
