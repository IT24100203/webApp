package lk.re_es.webApp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lk.re_es.webApp.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserController() {
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private File getFile() throws IOException {
        File file = new File("database/users.json");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            objectMapper.writeValue(file, new ArrayList<Person>());
        }

        return file;
    }

    private List<Person> readUsersFromFile() {
        try {
            File file = getFile();
            if (!file.exists()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(file, new TypeReference<List<Person>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeUsersToFile(List<Person> users) {
        try {
            objectMapper.writeValue(getFile(), users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Person user) {
        try {
            List<Person> users = readUsersFromFile();

            // Check if email already exists
            if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Email already registered");
            }

            // Generate ID
            Long newId = idGenerator.getAndIncrement();

            // Create new user with generated ID
            Person newUser = new Person(
                newId,
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword()
            );

            // Set role if provided, otherwise it will use default "USER"
            if (user.getRole() != null && !user.getRole().isEmpty()) {
                newUser.setRole(user.getRole());
            }

            // Add the new user
            users.add(newUser);

            // Save to file
            writeUsersToFile(users);

            // Don't send password in response
            newUser.setPassword(null);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Person loginRequest) {
        try {
            List<Person> users = readUsersFromFile();

            Person user = users.stream()
                    .filter(u -> u.getEmail().equals(loginRequest.getEmail()) &&
                            u.getPassword().equals(loginRequest.getPassword()))
                    .findFirst()
                    .orElse(null);

            if (user != null) {
                // Don't send password in response
                user.setPassword(null);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during login: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<Person> users = readUsersFromFile();
            // Remove passwords from response
            users.forEach(user -> user.setPassword(null));
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching users: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            List<Person> users = readUsersFromFile();
            Person user = users.stream()
                    .filter(u -> u.getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (user != null) {
                user.setPassword(null); // Don't send password
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with id: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching user: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Person updatedUser) {
        try {
            List<Person> users = readUsersFromFile();
            
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(id)) {
                    // Preserve the password if not provided in update
                    if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty()) {
                        updatedUser.setPassword(users.get(i).getPassword());
                    }
                    updatedUser.setId(id); // Ensure ID remains the same
                    users.set(i, updatedUser);
                    writeUsersToFile(users);
                    updatedUser.setPassword(null); // Don't send password in response
                    return ResponseEntity.ok(updatedUser);
                }
            }
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating user: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            List<Person> users = readUsersFromFile();
            
            boolean removed = users.removeIf(u -> u.getId().equals(id));
            
            if (removed) {
                writeUsersToFile(users);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found with id: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }
    }
}

