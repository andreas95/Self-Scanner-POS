package com.andreas.SelfScannerPOS.main.java;

import com.andreas.SelfScannerPOS.main.java.common.ScreenController;
import com.andreas.SelfScannerPOS.main.java.common.Shared;
import com.andreas.SelfScannerPOS.main.java.common.StageManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // set application name
        Shared.appName = "Self Scanner POS";
        primaryStage.setTitle(Shared.appName);
        // set application version
        Shared.appVersion = "1.0";
        // set available languages
        Shared.availableLangs = new ArrayList<>(Arrays.asList("ro", "en", "it"));
        // set language
        Shared.language = Shared.availableLangs.get(0);
        // set TVA (Tax) value %
        Shared.TVA = 21;
        // set currency symbol
        Shared.currency = "$";
        // set discount codes
        Shared.discountCodes = new HashMap<String, Integer>() {{
            put("discount10", 10);
            put("discount15", 15);
            put("discount20", 20);
            put("discount25", 25);
            put("discount30", 30);
            put("discount35", 35);
            put("discount40", 40);
        }};
        // set fullscreen size & remove standard message
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        // set application icon
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        // set first screen
        Shared.screen = ScreenController.Screen.WELCOME;
        // create first scene
        // set min width and height
        primaryStage.setMinWidth(1200.0);
        primaryStage.setMinHeight(750.0);
        new StageManager(primaryStage);
    }

    public static void main(String[] args) { launch(args);}
}



