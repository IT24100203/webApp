package lk.re_es.webApp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    public UserController() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty printing
    }

    // Helper method to get the file where users are stored
    private File getFile() throws IOException {
        File file = new File("database/users.json");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            objectMapper.writeValue(file, new ArrayList<User>());
        }

        return file;
    }

    private List<User> readUsersFromFile() {
        try {
            File file = getFile();
            if (!file.exists()) {
                return new ArrayList<>();
            }
            List<User> users = objectMapper.readValue(file, new TypeReference<List<User>>() {
            });
            System.out.println("Users read from file: " + users);
            return users;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeUsersToFile(List<User> users) {
        try {
            objectMapper.writeValue(getFile(), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() throws IOException {
        List<User> users = readUsersFromFile();

        for (User user : users) {
            System.out.println(user.getDetails());
        }

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User newUser) {
        try {
            File file = getFile();
            List<User> users = file.exists()
                    ? objectMapper.readValue(file, new TypeReference<List<User>>() {
            })
                    : new ArrayList<>();

            if (newUser.getRole() == null || newUser.getRole().trim().isEmpty()) {
                newUser.setRole("user");
            }

            Long nextId = users.isEmpty() ? 1L : users.get(users.size() - 1).getId() + 1;
            newUser.setId(nextId);

            users.add(newUser);
            objectMapper.writeValue(file, users);

            return ResponseEntity.ok("User added successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        try {
            List<User> users = readUsersFromFile();

            for (User user : users) {
                if (user.getEmail().equalsIgnoreCase(loginRequest.getEmail())) {
                    if (user.getPassword().equals(loginRequest.getPassword())) {
                        LoginResponse loginResponse = new LoginResponse(user.getName(), user.getEmail(), user.getPhone(), user.getPassword(), user.getDob(), user.getRole());
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

    @PutMapping("/{email}")
    public ResponseEntity<String> updateUser(@PathVariable String email, @RequestBody User updatedUser) {
        List<User> users = readUsersFromFile();
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) {
                updatedUser.setEmail(email);
                updatedUser.setId(users.get(i).getId());
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

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        List<User> users = readUsersFromFile();
        boolean removed = users.removeIf(user -> user.getEmail().equals(email));
        if (removed) {
            writeUsersToFile(users);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws IOException {
        List<User> users = readUsersFromFile();

        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(null);
            }
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");
        }

        Long nextId = 1L;
        if (!users.isEmpty()) {
            Long lastUserId = users.get(users.size() - 1).getId();
            if (lastUserId != null) {
                nextId = lastUserId + 1;
            }
        }
        user.setId(nextId);

        users.add(user);
        writeUsersToFile(users);

        return ResponseEntity.ok(user);
    }
}

