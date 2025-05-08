package org.example.project1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

public class RoomDesigner2D extends Application {

    private Pane canvas;
    private Group selectedFurniture;
    private Color roomColor = Color.BEIGE;

    private final Image[] furnitureImages = {
            new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/office_chair.png"),
            new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/armchair.jpg"),
            new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/table.jpg"),
            new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/table_with_chairs.jpg"),
            new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/tv_stand.jpg"),
            new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/table.jpg"),
            new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/obj7.png"),
            new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/wood_table.jpg")
    };

    @Override
    public void start(Stage stage) {
        canvas = new Pane();
        canvas.setStyle("-fx-background-color: beige;");
        canvas.setPrefSize(800, 600);

        HBox topBar = createTopBar();

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(canvas);

        Scene scene = new Scene(root, 900, 650);
        setupKeyboardControls(scene);

        stage.setTitle("2D Room Designer");
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar() {
        Button[] buttons = new Button[8];
        for (int i = 0; i < 8; i++) {
            ImageView view = new ImageView(furnitureImages[i]);
            view.setFitWidth(40);
            view.setFitHeight(40);

            buttons[i] = new Button();
            buttons[i].setGraphic(view);
            int index = i + 1;
            buttons[i].setOnAction(e -> addFurniture2D("object" + index, furnitureImages[index - 1]));
        }

        HBox buttonBox = new HBox(10, buttons);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button saveBtn = new Button("💾 Save");
        Button loadBtn = new Button("📂 Open");

        ColorPicker colorPicker = new ColorPicker(Color.BEIGE);
        colorPicker.setTooltip(new Tooltip("Change Room Color"));
        colorPicker.setOnAction(e -> {
            roomColor = colorPicker.getValue();
            canvas.setStyle("-fx-background-color: " + toWebColor(roomColor) + ";");
        });

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(15, buttonBox, spacer, loadBtn, saveBtn, colorPicker);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: lightgray;");
        return topBar;
    }

    private void addFurniture2D(String type, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);

        Group item = new Group(imageView);
        item.setTranslateX(Math.random() * 600);
        item.setTranslateY(Math.random() * 400);
        item.setUserData(type);

        item.setOnMouseClicked(e -> selectFurniture(item));
        canvas.getChildren().add(item);
    }

    private void selectFurniture(Group item) {
        if (selectedFurniture != null) {
            selectedFurniture.setEffect(null);
        }
        selectedFurniture = item;
        DropShadow glow = new DropShadow(15, Color.DODGERBLUE);
        selectedFurniture.setEffect(glow);
        selectedFurniture.toFront();
    }

    private void setupKeyboardControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (selectedFurniture == null) return;

            double move = 10;
            switch (event.getCode()) {
                case UP -> selectedFurniture.setTranslateY(selectedFurniture.getTranslateY() - move);
                case DOWN -> selectedFurniture.setTranslateY(selectedFurniture.getTranslateY() + move);
                case LEFT -> selectedFurniture.setTranslateX(selectedFurniture.getTranslateX() - move);
                case RIGHT -> selectedFurniture.setTranslateX(selectedFurniture.getTranslateX() + move);
                case DELETE -> {
                    canvas.getChildren().remove(selectedFurniture);
                    selectedFurniture = null;
                }
            }
        });
    }

    private String toWebColor(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public static void main(String[] args) {
        launch();
    }
}
