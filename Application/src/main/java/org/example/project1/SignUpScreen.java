package org.example.project1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        root.setAlignment(Pos.TOP_CENTER);

        // --- Logo or header image at top ---
        Image logo = new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/office_chair.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(120);
        logoView.setPreserveRatio(true);

        // --- Title ---
        Label title = new Label("Create Your Account");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // --- Form Fields ---
        GridPane form = new GridPane();
        form.setVgap(12);
        form.setHgap(10);
        form.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Username:");
        TextField username = new TextField();
        username.setPromptText("Choose a username");

        Label passLabel = new Label("Password:");
        PasswordField password = new PasswordField();
        password.setPromptText("Create a password");

        form.add(userLabel, 0, 0);
        form.add(username, 1, 0);
        form.add(passLabel, 0, 1);
        form.add(password, 1, 1);

        // --- Buttons and Feedback ---
        Button registerBtn = new Button("Register");
        Button backBtn = new Button("Back to Login");

        HBox buttonBox = new HBox(10, registerBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        Label message = new Label();
        message.setStyle("-fx-font-size: 13px;");

        // --- Registration Logic ---
        registerBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                message.setText("⚠ Please fill in all fields.");
                message.setStyle("-fx-text-fill: red;");
            } else if (userDatabase.containsKey(user)) {
                message.setText("⚠ Username already exists. Try another one.");
                message.setStyle("-fx-text-fill: red;");
            } else {
                userDatabase.put(user, pass);
                UserStorage.saveUsers(userDatabase);
                message.setText("✅ Registration successful! You can now log in.");
                message.setStyle("-fx-text-fill: green;");
                username.clear();
                password.clear();
            }
        });

        backBtn.setOnAction(e -> new LoginScreen(userDatabase).show(primaryStage));

        // --- Add all components to root layout ---
        root.getChildren().addAll(logoView, title, form, buttonBox, message);

        // --- Set scene and stage ---
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Sign Up");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
