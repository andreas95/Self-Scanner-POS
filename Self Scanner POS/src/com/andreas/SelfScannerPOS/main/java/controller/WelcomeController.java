package com.andreas.SelfScannerPOS.main.java.controller;

import com.andreas.SelfScannerPOS.main.java.common.ScreenController;
import com.andreas.SelfScannerPOS.main.java.common.Shared;
import com.andreas.SelfScannerPOS.main.java.common.StageManager;
import com.andreas.SelfScannerPOS.main.java.utils.Transitions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeController implements Initializable {
    @FXML
    private StackPane MainPane;
    private int toggleBackground = 1;
    @FXML
    private HBox LanguagesBox;
    private Label[] flags = new Label[Shared.availableLangs.size()];
    @FXML
    private Button StartButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Transitions.fadeInPanel(MainPane);

        MainPane.getStyleClass().add("background1");

        for (int counter = 0; counter < Shared.availableLangs.size(); counter++) {
            Label flag = new Label();
            ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/flags/" + Shared.availableLangs.get(counter) + ".png")));
            img.setFitWidth(64.0);
            img.setFitHeight(64.0);
            flag.setGraphic(img);
            flag.getStyleClass().add("click");
            if (Shared.availableLangs.get(counter).equals(Shared.language)) flag.getStyleClass().add("selected-lang");
            flags[counter] = flag;
            LanguagesBox.getChildren().add(flag);
        }
        int idx = 0;
        for (Label flag : flags) {
            int finalIdx = idx;
            flag.setOnMouseClicked(e -> {
                for (Label flg : flags) {
                    if (flg.getStyleClass().contains("selected-lang")) flg.getStyleClass().remove("selected-lang");
                }
                flag.getStyleClass().add("selected-lang");
                Shared.language = Shared.availableLangs.get(finalIdx);
                StageManager.updateLanguage();
            });
            idx++;
        }

        StartButton.setOnAction(e -> ScreenController.changeScreen(ScreenController.Screen.SHOPPING));

        new Timer().schedule(
                new TimerTask() {

                    @Override
                    public void run() {
                        if (toggleBackground == 1) {
                            toggleBackground = 2;
                            MainPane.getStyleClass().remove("background2");
                            MainPane.getStyleClass().add("background1");
                        } else {
                            toggleBackground = 1;
                            MainPane.getStyleClass().remove("background1");
                            MainPane.getStyleClass().add("background2");
                        }
                    }
                }, 0, 30000);
    }
}
