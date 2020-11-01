package com.andreas.SelfScannerPOS.main.java.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class StageManager {
    private static Stage stage;

    public StageManager(Stage stage) {
        this.stage = stage;
        try {
            stage.setScene(new Scene(FXMLLoader.load(StageManager.class.getResource(ScreenController.getScreen(Shared.screen)),
                    ResourceBundle.getBundle("translations.lang", new Locale(Shared.language)))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }

    public static Stage getStage() { return stage; }

    public static void changeScreen() {
        try {
            StageManager.stage.getScene().setRoot(FXMLLoader.load(ScreenController.class.getResource(ScreenController.getScreen(Shared.screen)),
                    ResourceBundle.getBundle("translations.lang", new Locale(Shared.language))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateLanguage() {
        try {
            StageManager.stage.getScene().setRoot(FXMLLoader.load(ScreenController.class.getResource(ScreenController.getScreen(Shared.screen)),
                    ResourceBundle.getBundle("translations.lang", new Locale(Shared.language))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
