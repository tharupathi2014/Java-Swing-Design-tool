package org.example.project1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen {

    private final Map<String, String> userDatabase;

    public LoginScreen() {
        this.userDatabase = new HashMap<>();
        // Preload with default admin
        userDatabase.put("admin", "admin");
    }

    public LoginScreen(Map<String, String> sharedDb) {
        this.userDatabase = sharedDb;
    }

    public void show(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Furniture Designer Login");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Username:");
        TextField username = new TextField();

        Label passLabel = new Label("Password:");
        PasswordField password = new PasswordField();

        form.add(userLabel, 0, 0);
        form.add(username, 1, 0);
        form.add(passLabel, 0, 1);
        form.add(password, 1, 1);

        Button loginBtn = new Button("Login");
        Button signUpBtn = new Button("Sign Up");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText().trim();

            if (userDatabase.containsKey(user) && userDatabase.get(user).equals(pass)) {
                RoomDesigner3D designer = new RoomDesigner3D();
                designer.start(primaryStage);
            } else {
                message.setText("Invalid credentials.");
            }
        });

        signUpBtn.setOnAction(e -> {
            SignUpScreen signUpScreen = new SignUpScreen(userDatabase);
            signUpScreen.show(primaryStage);
        });

        HBox buttons = new HBox(10, loginBtn, signUpBtn);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, form, buttons, message);

        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
}
