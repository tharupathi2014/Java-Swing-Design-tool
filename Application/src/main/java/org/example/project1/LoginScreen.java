package org.example.project1;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen {

    public void show(Stage primaryStage) {
        // Layout
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
        username.setPromptText("Enter username");

        Label passLabel = new Label("Password:");
        PasswordField password = new PasswordField();
        password.setPromptText("Enter password");

        form.add(userLabel, 0, 0);
        form.add(username, 1, 0);
        form.add(passLabel, 0, 1);
        form.add(password, 1, 1);

        Button loginBtn = new Button("Login");
        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        // Login Logic
        loginBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText().trim();

            if (user.equals("admin") && pass.equals("admin")) {
                // Proceed to 3D designer
                RoomDesigner3D designer = new RoomDesigner3D();
                designer.start(primaryStage);
            } else {
                message.setText("Invalid credentials. Try 'admin' / 'admin'.");
            }
        });

        root.getChildren().addAll(title, form, loginBtn, message);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
