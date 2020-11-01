package com.andreas.SelfScannerPOS.main.java.common;

public class ScreenController {

    public static enum Screen { WELCOME, SHOPPING }

    public ScreenController() {}

    public static void changeScreen(Screen screen) {
        switch (screen) {
            case WELCOME:
                Shared.screen= Screen.WELCOME;
                StageManager.changeScreen();
                break;
            case SHOPPING:
                Shared.screen= Screen.SHOPPING;
                StageManager.changeScreen();
                break;
        }
    }

    public static String getScreen(Screen screen) {
        switch (screen) {
            case WELCOME:
                return "/view/WelcomeView.fxml";
            case SHOPPING:
                return "/view/ShoppingView.fxml";
            default:
                return "/view/WelcomeView.fxml";
        }
    }
}
