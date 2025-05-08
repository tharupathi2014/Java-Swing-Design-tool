package org.example.project1;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load users from file (including "admin" if already saved)
        Map<String, String> userDatabase = UserStorage.loadUsers();

        // Optionally preload admin if not present
        userDatabase.putIfAbsent("admin", "admin");

        // Show login screen with loaded users
        new LoginScreen(userDatabase).show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
