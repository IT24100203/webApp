package com.feedback.app.service.impl;

import com.feedback.app.model.User;
import com.feedback.app.service.UserService;
import com.feedback.app.util.FileStorageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {
    
    private final FileStorageUtil<User> fileStorage;
    
    /**
     * Constructor
     */
    public UserServiceImpl() {
        this.fileStorage = new FileStorageUtil<>("data/users.dat", User.class);
        
        // Add a default admin user if no users exist
        if (getAllUsers().isEmpty()) {
            User admin = new User(
                    UUID.randomUUID().toString(),
                    "admin",
                    "admin123",
                    "admin@example.com",
                    "Administrator"
            );
            saveUser(admin);
            
            // Add a regular user for testing
            User user = new User(
                    UUID.randomUUID().toString(),
                    "user",
                    "user123",
                    "user@example.com",
                    "Regular User"
            );
            saveUser(user);
        }
    }
    
    @Override
    public List<User> getAllUsers() {
        return fileStorage.getAll();
    }
    
    @Override
    public Optional<User> getUserById(String id) {
        return fileStorage.findOne(user -> user.getId().equals(id));
    }
    
    @Override
    public Optional<User> getUserByUsername(String username) {
        return fileStorage.findOne(user -> user.getUsername().equals(username));
    }
    
    @Override
    public User saveUser(User user) {
        // Generate ID if not provided
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        
        return fileStorage.save(user);
    }
    
    @Override
    public Optional<User> authenticate(String username, String password) {
        return fileStorage.findOne(user -> 
                user.getUsername().equals(username) && 
                user.getPassword().equals(password));
    }
}
