package com.andreas.SelfScannerPOS.main.java.utils;

import com.andreas.SelfScannerPOS.main.java.common.StageManager;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AlertMessage {

    public AlertMessage(String title, String content, Alert.AlertType type, String buttonText) {
        Alert alert = new Alert(type,content);
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.initOwner(StageManager.getStage());
        ButtonType done = new ButtonType(buttonText, ButtonBar.ButtonData.LEFT);
        alert.getButtonTypes().setAll(done);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        dialogPane.getStyleClass().add("assistance-dialog");
        alert.setDialogPane(dialogPane);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> alert.close());
        delay.play();
        alert.showAndWait();
    }
}
