package org.example.project1;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.Shape3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple OBJ loader for very basic .obj files with vertices (v) and faces (f).
 * This does not handle textures, normals, or advanced materials.
 */
public class ObjModelLoader {

    public static MeshView load(String path) throws Exception {
        InputStream is;
        if (path.startsWith("C:") || path.startsWith("/")) {
            is = new FileInputStream(path); // absolute path
        } else {
            is = ObjModelLoader.class.getClassLoader().getResourceAsStream(path); // classpath
        }

        if (is == null) {
            throw new IllegalArgumentException("Model file not found: " + path);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        List<Float> vertices = new ArrayList<>();
        List<Integer> faces = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                String[] tokens = line.split("\\s+");
                vertices.add(Float.parseFloat(tokens[1]));
                vertices.add(Float.parseFloat(tokens[2]));
                vertices.add(Float.parseFloat(tokens[3]));
            } else if (line.startsWith("f ")) {
                String[] tokens = line.split("\\s+");
                for (int i = 1; i <= 3; i++) {
                    String[] parts = tokens[i].split("/");
                    int vertexIndex = Integer.parseInt(parts[0]) - 1;
                    faces.add(vertexIndex);
                }
            }
        }

        float[] pointsArray = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) pointsArray[i] = vertices.get(i);

        int[] facesArray = new int[faces.size() * 2]; // JavaFX requires indices for points & texCoords
        for (int i = 0, j = 0; i < faces.size(); i++) {
            facesArray[j++] = faces.get(i);
            facesArray[j++] = 0; // All faces use the same texCoord
        }

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().setAll(pointsArray);
        mesh.getTexCoords().addAll(0, 0); // Dummy texCoord
        mesh.getFaces().setAll(facesArray);

        MeshView view = new MeshView(mesh);
        view.setMaterial(new PhongMaterial(Color.LIGHTGRAY));
        return view;
    }
}
