package org.example.project1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Map;

public class SignUpScreen {

    private final Map<String, String> userDatabase;

    public SignUpScreen(Map<String, String> userDatabase) {
        this.userDatabase = userDatabase;
    }

    public void show(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Sign Up");
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

        Button registerBtn = new Button("Register");
        Label message = new Label();
        message.setStyle("-fx-text-fill: green;");

        registerBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                message.setText("Please fill in all fields.");
            } else if (userDatabase.containsKey(user)) {
                message.setText("Username already exists.");
            } else {
                userDatabase.put(user, pass);
                message.setText("Registration successful! Return to login.");
            }
        });

        Button backBtn = new Button("Back to Login");
        backBtn.setOnAction(e -> new LoginScreen(userDatabase).show(primaryStage));

        root.getChildren().addAll(title, form, registerBtn, message, backBtn);

        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.setTitle("Sign Up");
    }
}
