package org.example.project1;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import javafx.scene.control.ChoiceDialog;
import javafx.util.StringConverter;

import javafx.scene.shape.MeshView;
import javafx.scene.image.Image;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;

public class RoomDesigner3D extends Application {

    private double anchorX, anchorY;
    private double angleX = 0;
    private double angleY = 0;
    private Group world = new Group();
    private Node selectedNode = null;
    private Color roomColor = Color.BEIGE;

    private String currentUser = "guest";

    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    public void saveCurrentDesign() {
        TextInputDialog dialog = new TextInputDialog("my_design");
        dialog.setTitle("Save Design");
        dialog.setHeaderText("Enter a name for your design:");

        dialog.showAndWait().ifPresent(name -> {
            DesignStorage.saveDesign(currentUser, name, world.getChildren(), roomColor, angleX, angleY);
        });
    }

    @Override
    public void start(Stage stage) {
        // Create floor and walls
        Box floor = createBox(600, 5, 600, Color.LIGHTGRAY, 0, 200, 0);
        Box wallBack = createBox(600, 400, 5, Color.BEIGE, 0, 0, 300);
        Box wallLeft = createBox(5, 400, 600, Color.BEIGE, -300, 0, 0);
        world.getChildren().addAll(floor, wallBack, wallLeft);

        // Load images
        Image[] images = {
                new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/office_chair.png"),
                new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/armchair.jpg"),
                new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/table.jpg"),
                new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/table_with_chairs.jpg"),
                new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/tv_stand.jpg"),
                new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/table.jpg"),
                new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/obj7.png"),
                new Image("file:/C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/images/wood_table.jpg")
        };

        Button[] buttons = new Button[8];
        for (int i = 0; i < 8; i++) {
            ImageView view = new ImageView(images[i]);
            view.setFitWidth(40);
            view.setFitHeight(40);
            buttons[i] = new Button();
            buttons[i].setGraphic(view);
            int index = i + 1;
            buttons[i].setOnAction(e -> addFurniture("object" + index));
        }

        HBox leftButtons = new HBox(10, buttons);
        leftButtons.setAlignment(Pos.CENTER_LEFT);

        // Save and Load buttons
        Button saveBtn = new Button("💾 Save");
        saveBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> saveCurrentDesign());

        Button loadBtn = new Button("📂 Open Design");
        loadBtn.setStyle("-fx-font-size: 14px;");
        loadBtn.setOnAction(e -> loadCurrentDesign());

        // Color Picker
        ColorPicker colorPicker = new ColorPicker(Color.BEIGE);
        colorPicker.setTooltip(new Tooltip("Change Room Color"));
        colorPicker.setOnAction(e -> {
            roomColor = colorPicker.getValue();
            Color selected = colorPicker.getValue();
            floor.setMaterial(new PhongMaterial(selected));
            wallBack.setMaterial(new PhongMaterial(selected));
            wallLeft.setMaterial(new PhongMaterial(selected));
        });

        // Top bar layout
        HBox spacer = new HBox();
        spacer.setMinWidth(Region.USE_COMPUTED_SIZE);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(15, leftButtons, spacer, loadBtn, saveBtn, colorPicker);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: lightgray;");

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(5000);
        camera.setFieldOfView(35);

        // Rotation
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        world.getTransforms().addAll(rotateX, rotateY);

        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.SKYBLUE);
        subScene.setCamera(camera);

        subScene.setOnMousePressed(e -> {
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
        });

        subScene.setOnMouseDragged(e -> {
            if (e.isSecondaryButtonDown()) {
                angleY += (e.getSceneX() - anchorX) * 0.3;
                angleX -= (e.getSceneY() - anchorY) * 0.3;
                rotateY.setAngle(angleY);
                rotateX.setAngle(angleX);
                anchorX = e.getSceneX();
                anchorY = e.getSceneY();
            }
        });

        // Final layout
        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(subScene);

        Scene scene = new Scene(root, 900, 650, true);

        scene.setOnKeyPressed(event -> {
            if (selectedNode != null) {
                switch (event.getCode()) {
                    case R -> selectedNode.getTransforms().add(new Rotate(15, Rotate.Y_AXIS));
                    case X -> selectedNode.getTransforms().add(new Rotate(15, Rotate.X_AXIS));
                    case Z -> selectedNode.getTransforms().add(new Rotate(15, Rotate.Z_AXIS));
                    case EQUALS, ADD -> scaleNode(selectedNode, 1.1);
                    case MINUS, SUBTRACT -> scaleNode(selectedNode, 0.9);
                }
            }
        });

        scene.setOnScroll(e -> {
            double zoomFactor = 20;
            double delta = e.getDeltaY();
            camera.setTranslateZ(camera.getTranslateZ() + (delta > 0 ? zoomFactor : -zoomFactor));
        });

        stage.setTitle("3D Furniture Room Designer");
        stage.setScene(scene);
        stage.show();
    }

    public void loadCurrentDesign() {
        File userDir = new File("saved_designs", currentUser);
        if (!userDir.exists() || !userDir.isDirectory()) {
            System.out.println("No saved designs found for user: " + currentUser);
            return;
        }

        File[] designFiles = userDir.listFiles((dir, name) -> name.endsWith(".design"));
        if (designFiles == null || designFiles.length == 0) {
            System.out.println("No design files to load.");
            return;
        }

        // Create a list of design names (without extensions)
        List<String> designNames = Arrays.stream(designFiles)
                .map(file -> file.getName().replace(".design", ""))
                .toList();

        // Show dialog with just design names
        ChoiceDialog<String> dialog = new ChoiceDialog<>(designNames.get(0), designNames);
        dialog.setTitle("Load Design");
        dialog.setHeaderText("Select a saved design to load:");
        dialog.setContentText("Designs:");

        dialog.showAndWait().ifPresent(designName -> {
            File selectedFile = new File(userDir, designName + ".design");
            DesignStorage.loadDesignFile(selectedFile, this);
        });
    }

    private void scaleNode(Node node, double scaleFactor) {
        node.setScaleX(node.getScaleX() * scaleFactor);
        node.setScaleY(node.getScaleY() * scaleFactor);
        node.setScaleZ(node.getScaleZ() * scaleFactor);
    }

    public MeshView loadFurnitureModel(String type) {
        System.out.println("Attempting to load model: " + type);
        try {
            MeshView model = null;
            switch (type) {
                case "object1" -> model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/table/table.obj");
                case "object2" -> model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/69-chairss-obj/chairss.obj");
                case "object3" -> model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object1/10222_Coffee_Table_v1_max2010vb.obj");
                case "object4" -> model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object2/Table And Chairs.obj");
                case "object5" -> model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object8/furniture_xena.obj");
                case "object6" -> model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object4/table3.obj");
                case "object7" -> model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/89-obj/the chair modeling.obj");
                case "object8" -> model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object7/Wood_Table.obj");
            }
            if (model == null) {
                System.out.println("Model load failed for type: " + type);
            }
            return model;
        } catch (Exception e) {
            System.err.println("Error loading model for " + type + ": " + e.getMessage());
            return null;
        }
    }

    public void addToScene(Node node) {
        enableFurnitureControls(node);
        world.getChildren().add(node);
    }


    private Box createBox(double w, double h, double d, Color color, double x, double y, double z) {
        Box box = new Box(w, h, d);
        box.setMaterial(new PhongMaterial(color));
        box.setTranslateX(x);
        box.setTranslateY(y);
        box.setTranslateZ(z);
        return box;
    }

    private void enableFurnitureControls(Node item) {
        selectedNode = item;

        // Remove any old arrows
        world.getChildren().removeIf(n -> n.getUserData() != null && n.getUserData().equals("arrow"));

        double size = 20;
        double offset = 50;

        // Create 6 direction arrows (boxes)
        createArrow("X+", offset, 0, 0, item, size, Color.RED);
        createArrow("X-", -offset, 0, 0, item, size, Color.RED);
        createArrow("Y+", 0, -offset, 0, item, size, Color.GREEN);
        createArrow("Y-", 0, offset, 0, item, size, Color.GREEN);
        createArrow("Z+", 0, 0, offset, item, size, Color.BLUE);
        createArrow("Z-", 0, 0, -offset, item, size, Color.BLUE);
    }

    private void createArrow(String direction, double dx, double dy, double dz, Node target, double size, Color color) {
        Box arrow = new Box(size / 3, size / 3, size / 3);
        arrow.setMaterial(new PhongMaterial(color));
        arrow.setTranslateX(target.getTranslateX() + dx);
        arrow.setTranslateY(target.getTranslateY() + dy);
        arrow.setTranslateZ(target.getTranslateZ() + dz);
        arrow.setUserData("arrow");

        arrow.setOnMouseClicked(e -> {
            double step = 10;
            switch (direction) {
                case "X+" -> target.setTranslateX(target.getTranslateX() + step);
                case "X-" -> target.setTranslateX(target.getTranslateX() - step);
                case "Y+" -> target.setTranslateY(target.getTranslateY() - step); // up is -Y
                case "Y-" -> target.setTranslateY(target.getTranslateY() + step);
                case "Z+" -> target.setTranslateZ(target.getTranslateZ() + step);
                case "Z-" -> target.setTranslateZ(target.getTranslateZ() - step);
            }

            // Update arrow positions to follow the object
            enableFurnitureControls(target);
        });

        world.getChildren().add(arrow);
    }

    private void addFurniture(String type) {
        try {
            MeshView model = null;

            switch (type) {
                case "object1" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/table/table.obj");
                    model.setMaterial(new PhongMaterial(Color.BEIGE));
                }
                case "object2" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/69-chairss-obj/chairss.obj");
                    model.setMaterial(new PhongMaterial(Color.DARKRED));
                }
                case "object3" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object1/10222_Coffee_Table_v1_max2010vb.obj");
                    model.setMaterial(new PhongMaterial(Color.DARKBLUE));
                }
                case "object4" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object2/Table And Chairs.obj");
                    model.setMaterial(new PhongMaterial(Color.DARKGREEN));
                }
                case "object5" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object8/furniture_xena.obj");
                    model.setMaterial(new PhongMaterial(Color.DARKGREEN));
                }
                case "object6" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object4/table3.obj");
                    model.setMaterial(new PhongMaterial(Color.DARKGREEN));
                }
                case "object7" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/89-obj/the chair modeling.obj");
                    model.setMaterial(new PhongMaterial(Color.BEIGE));
                }
                case "object8" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/object7/Wood_Table.obj");
                    model.setMaterial(new PhongMaterial(Color.DARKGREEN));
                }
                default -> {
                    System.out.println("Unknown furniture type: " + type);
                    return;
                }
            }

            // Randomized position on each call
            model.setScaleX(10);
            model.setScaleY(10);
            model.setScaleZ(10);
            model.setTranslateX(Math.random() * 400 - 200); // Wider range
            model.setTranslateY(180);
            model.setTranslateZ(Math.random() * 400 - 200);

            model.setUserData(type);
            enableFurnitureControls(model);
            world.getChildren().add(model);

        } catch (Exception ex) {
            System.err.println("Failed to load model for " + type + ": " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        launch();
    }
}
