package org.example.project1;
import javafx.application.Application;import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import javafx.scene.shape.MeshView;

public class RoomDesigner3D extends Application {

    private double anchorX, anchorY;
    private double angleX = 0;
    private double angleY = 0;
    private Group world = new Group();
    private Node selectedNode = null;

    @Override
    public void start(Stage stage) {
        // Create floor and walls
        Box floor = createBox(600, 5, 600, Color.LIGHTGRAY, 0, 200, 0);
        Box wallBack = createBox(600, 400, 5, Color.BEIGE, 0, 0, 300);
        Box wallLeft = createBox(5, 400, 600, Color.BEIGE, -300, 0, 0);

        world.getChildren().addAll(floor, wallBack, wallLeft);

        // Add camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(5000);
        camera.setFieldOfView(35);

        // Enable world rotation
        Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
        world.getTransforms().addAll(rotateX, rotateY);

        SubScene subScene = new SubScene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.SKYBLUE);
        subScene.setCamera(camera);

        // Mouse drag to rotate the room
        subScene.setOnMousePressed(e -> {
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
        });

        subScene.setOnMouseDragged(e -> {
            if (e.isSecondaryButtonDown()) { // right-click only
                angleY += (e.getSceneX() - anchorX) * 0.3;
                angleX -= (e.getSceneY() - anchorY) * 0.3;
                rotateY.setAngle(angleY);
                rotateX.setAngle(angleX);
                anchorX = e.getSceneX();
                anchorY = e.getSceneY();
            }
        });

        // UI buttons
        Button addTable = new Button("Add Table");
        Button addChair1 = new Button("Add Couch");
        Button addChair2 = new Button("Add Modern Desk");
        Button addChair3 = new Button("Add Classic Desk");

        addChair1.setOnAction(e -> addFurniture("chair1"));
        addTable.setOnAction(e -> addFurniture("table"));
        addChair2.setOnAction(e -> addFurniture("chair2"));
        addChair3.setOnAction(e -> addFurniture("chair3"));

        HBox controls = new HBox(10, addTable, addChair1, addChair2, addChair3);
        controls.setStyle("-fx-padding: 10; -fx-background-color: lightgray;");

        BorderPane root = new BorderPane();
        root.setTop(controls);
        root.setCenter(subScene);

        Scene scene = new Scene(root, 900, 650, true);

        scene.setOnKeyPressed(event -> {
            if (selectedNode != null) {
                switch (event.getCode()) {
                    case R -> selectedNode.getTransforms().add(new Rotate(15, Rotate.Y_AXIS)); // rotate left/right
                    case X -> selectedNode.getTransforms().add(new Rotate(15, Rotate.X_AXIS)); // tilt forward/back
                    case Z -> selectedNode.getTransforms().add(new Rotate(15, Rotate.Z_AXIS)); // tilt sideways
                }
            }
        });

        scene.setOnScroll(e -> {
            double zoomFactor = 20;
            double delta = e.getDeltaY();

            // Zoom in (scroll up) = move camera closer (more negative Z)
            if (delta > 0) {
                camera.setTranslateZ(camera.getTranslateZ() + zoomFactor);
            }
            // Zoom out (scroll down) = move camera back (less negative Z)
            else {
                camera.setTranslateZ(camera.getTranslateZ() - zoomFactor);
            }
        });

        scene.setOnKeyPressed(event -> {
            if (selectedNode != null) {
                switch (event.getCode()) {
                    case R -> selectedNode.getTransforms().add(new Rotate(15, Rotate.Y_AXIS));
                    case X -> selectedNode.getTransforms().add(new Rotate(15, Rotate.X_AXIS));
                    case Z -> selectedNode.getTransforms().add(new Rotate(15, Rotate.Z_AXIS));
                    case EQUALS, ADD -> scaleNode(selectedNode, 1.1); // scale up
                    case MINUS, SUBTRACT -> scaleNode(selectedNode, 0.9); // scale down
                }
            }
        });

        stage.setTitle("3D Furniture Room Designer");
        stage.setScene(scene);
        stage.show();
    }

    private void scaleNode(Node node, double scaleFactor) {
        node.setScaleX(node.getScaleX() * scaleFactor);
        node.setScaleY(node.getScaleY() * scaleFactor);
        node.setScaleZ(node.getScaleZ() * scaleFactor);
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
                case "table" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/table/table.obj");
                    model.setMaterial(new PhongMaterial(Color.BEIGE));
                }
                case "chair1" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/New folder/Java-Swing-Design-tool/Application/src/main/resources/assets/69-chairss-obj/chairss.obj");
                    model.setMaterial(new PhongMaterial(Color.DARKRED));
                }
                case "chair2" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/project1/src/main/resources/assets/table2/Textures/ModernDeskOBJ.obj");
                    model.setMaterial(new PhongMaterial(Color.DARKBLUE));
                }
                case "chair3" -> {
                    model = ObjModelLoader.load("C:/Users/USER/Desktop/project1/src/main/resources/assets/table3/desk.obj");
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
