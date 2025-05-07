package org.example.project1;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private final Map<String, String> userDatabase = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        // Preload admin credentials
        userDatabase.put("admin", "admin");

        // Pass shared DB to Login screen
        new LoginScreen(userDatabase).show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
