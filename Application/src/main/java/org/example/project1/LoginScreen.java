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

public class LoginScreen {

    private final Map<String, String> userDatabase;

    public LoginScreen(Map<String, String> userDatabase) {
        this.userDatabase = userDatabase;
    }

    public void show(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        // --- Logo or banner ---
        Image logo = new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/office_chair.png");
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(120);
        logoView.setPreserveRatio(true);

        // --- Title ---
        Label title = new Label("Welcome Back");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // --- Form Fields ---
        GridPane form = new GridPane();
        form.setVgap(12);
        form.setHgap(10);
        form.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Username:");
        TextField username = new TextField();
        username.setPromptText("Enter your username");

        Label passLabel = new Label("Password:");
        PasswordField password = new PasswordField();
        password.setPromptText("Enter your password");

        form.add(userLabel, 0, 0);
        form.add(username, 1, 0);
        form.add(passLabel, 0, 1);
        form.add(password, 1, 1);

        // --- Buttons and Feedback ---
        Button loginBtn = new Button("Login");
        Button signUpBtn = new Button("Sign Up");

        HBox buttonBox = new HBox(10, loginBtn, signUpBtn);
        buttonBox.setAlignment(Pos.CENTER);

        Label message = new Label();
        message.setStyle("-fx-font-size: 13px;");

        // --- Login Logic ---
        loginBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                message.setText("⚠ Please fill in both fields.");
                message.setStyle("-fx-text-fill: red;");
            } else if (userDatabase.containsKey(user) && userDatabase.get(user).equals(pass)) {
                RoomDesigner3D designer = new RoomDesigner3D();
                designer.setCurrentUser(user);     // Store username
                designer.start(primaryStage);
                designer.loadCurrentDesign();      // Load saved design
            } else {
                message.setText("Invalid username or password.");
                message.setStyle("-fx-text-fill: red;");
            }
        });

        signUpBtn.setOnAction(e -> new SignUpScreen(userDatabase).show(primaryStage));

        // --- Add all components to root layout ---
        root.getChildren().addAll(logoView, title, form, buttonBox, message);

        // --- Set scene and stage ---
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
