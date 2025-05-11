package lk.re_es.webApp.controller;

import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import lk.re_es.webApp.model.User;
import lk.re_es.webApp.dto.LoginResponse;


@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Helper method to get the file where users are stored
    private File getFile() throws IOException {
        File file = new File("database/users.json");

        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Ensure the directory exists
            file.createNewFile();
            objectMapper.writeValue(file, new ArrayList<User>()); // Initialize with an empty array
        }

        return file;
    }

    // Method to read users from the file
    private List<User> readUsersFromFile() {
        try {
            File file = getFile();
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Method to write the updated list of users back to the file
    private void writeUsersToFile(List<User> users) {
        try (FileWriter writer = new FileWriter(getFile())) {
            objectMapper.writeValue(writer, users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get all users (for admin or for viewing purposes)
    @GetMapping
    public ResponseEntity<List<User>> getUsers() throws IOException {
        return ResponseEntity.ok(readUsersFromFile());
    }

    // Add a new user
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        try {
            File file = getFile();
            List<User> users = file.exists()
                    ? objectMapper.readValue(file, new TypeReference<List<User>>() {})
                    : new ArrayList<>();

            // Set default role if none is provided
            if (newUser.getRole() == null || newUser.getRole().trim().isEmpty()) {
                newUser.setRole("user");
            }

            users.add(newUser);
            objectMapper.writeValue(file, users);

            return ResponseEntity.ok("User added successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding user: " + e.getMessage());
        }
    }

    // Login a user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        try {
            List<User> users = readUsersFromFile();

            for (User user : users) {
                if (user.getEmail().equalsIgnoreCase(loginRequest.getEmail())) {
                    if (user.getPassword().equals(loginRequest.getPassword())) {

                        LoginResponse loginResponse = new LoginResponse(user.getName(), user.getEmail(), user.getRole());
                        return ResponseEntity.ok(loginResponse);
                    } else {
                        return ResponseEntity.status(401).body("Incorrect password");
                    }
                }
            }

            return ResponseEntity.status(404).body("User not found");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }



    // Update a user (by email)
    @PutMapping("/{email}")
    public ResponseEntity<String> updateUser(@PathVariable String email, @RequestBody User updatedUser) {
        List<User> users = readUsersFromFile();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) {
                updatedUser.setEmail(email);
                users.set(i, updatedUser);
                writeUsersToFile(users);
                found = true;
                break;
            }
        }
        if (found) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // Delete a user (by email)
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        List<User> users = readUsersFromFile();
        boolean removed = users.removeIf(user -> user.getEmail().equals(email));
        if (removed) {
            writeUsersToFile(users); // Write the updated list back to the file
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws IOException {
        List<User> users = readUsersFromFile(); // Use the existing method to read users

        // Check if email already exists
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(null);
            }
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user"); // Default to "user" if no role is provided
        }

        users.add(user);
        writeUsersToFile(users);

        return ResponseEntity.ok(user); // Return the newly created user as the response
    }
}
