package org.example.project1;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserStorage {
    private static final String FILE_PATH = "users.txt";

    public static void saveUsers(Map<String, String> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
        }
    }

    public static Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user data: " + e.getMessage());
        }

        return users;
    }
}
