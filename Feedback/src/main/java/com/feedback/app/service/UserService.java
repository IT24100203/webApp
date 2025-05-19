package com.feedback.app.service;

import com.feedback.app.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for User operations.
 */
public interface UserService {
    
    /**
     * Get all users
     * @return List of all users
     */
    List<User> getAllUsers();
    
    /**
     * Get user by ID
     * @param id User ID
     * @return Optional containing user if found
     */
    Optional<User> getUserById(String id);
    
    /**
     * Get user by username
     * @param username Username
     * @return Optional containing user if found
     */
    Optional<User> getUserByUsername(String username);
    
    /**
     * Save a user
     * @param user User to save
     * @return Saved user
     */
    User saveUser(User user);
    
    /**
     * Authenticate a user
     * @param username Username
     * @param password Password
     * @return Optional containing user if authentication successful
     */
    Optional<User> authenticate(String username, String password);
}
