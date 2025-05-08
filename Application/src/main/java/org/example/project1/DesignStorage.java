package org.example.project1;

import javafx.scene.Node;
import javafx.scene.shape.MeshView;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.*;
import java.util.List;

public class DesignStorage {

    private static final String SAVE_DIR = "saved_designs";

    public static void saveDesign(String username, String designName, List<Node> nodes, Color roomColor, double angleX, double angleY) {
        File userDir = new File("saved_designs", username);
        if (!userDir.exists()) userDir.mkdirs();

        File file = new File(userDir, designName + ".design");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Save room color
            writer.write("COLOR " + toHexColor(roomColor) + "\n");

            // Save rotation angles
            writer.write(String.format("ROOM_ROTATION %.2f %.2f%n", angleX, angleY));

            // Save furniture
            for (Node node : nodes) {
                if (node instanceof MeshView mesh && node.getUserData() instanceof String type) {
                    writer.write(String.format(
                            "OBJECT %s %.2f %.2f %.2f %.2f %.2f %.2f%n",
                            type,
                            mesh.getTranslateX(),
                            mesh.getTranslateY(),
                            mesh.getTranslateZ(),
                            mesh.getScaleX(),
                            mesh.getScaleY(),
                            mesh.getScaleZ()
                    ));
                }
            }

        } catch (IOException e) {
            System.err.println("Error saving design: " + e.getMessage());
        }
    }

    private static String toHexColor(Color color) {
        return String.format("#%02x%02x%02x",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public static void loadDesignFile(File file, RoomDesigner3D designer) {
        System.out.println("Loading design from: " + file.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Line: " + line);

                String[] parts = line.trim().split("\\s+");

                // Allow optional "OBJECT" prefix
                int offset = parts[0].equalsIgnoreCase("OBJECT") ? 1 : 0;

                if (parts.length - offset < 7) {
                    System.out.println("⚠ Skipping invalid line: " + line);
                    continue;
                }

                try {
                    String type = parts[offset];
                    double x = Double.parseDouble(parts[offset + 1]);
                    double y = Double.parseDouble(parts[offset + 2]);
                    double z = Double.parseDouble(parts[offset + 3]);
                    double sx = Double.parseDouble(parts[offset + 4]);
                    double sy = Double.parseDouble(parts[offset + 5]);
                    double sz = Double.parseDouble(parts[offset + 6]);

                    System.out.println("➡ Loading model: " + type);

                    MeshView model = designer.loadFurnitureModel(type);
                    if (model != null) {
                        model.setTranslateX(x);
                        model.setTranslateY(y);
                        model.setTranslateZ(z);
                        model.setScaleX(sx);
                        model.setScaleY(sy);
                        model.setScaleZ(sz);
                        model.setUserData(type);

                        designer.addToScene(model);
                    } else {
                        System.out.println("Failed to load model for type: " + type);
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Failed to parse line: " + line);
                    ex.printStackTrace();
                }
            }

            System.out.println("Finished loading design.");
        } catch (IOException e) {
            System.err.println("Error loading design: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
