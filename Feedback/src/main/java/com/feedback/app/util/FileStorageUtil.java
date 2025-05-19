package com.feedback.app.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for file storage operations.
 * @param <T> Type of object to store
 */
public class FileStorageUtil<T> {
    
    private final String filePath;
    private final Class<T> type;
    
    /**
     * Constructor
     * @param filePath Path to the file
     * @param type Class of the object type
     */
    public FileStorageUtil(String filePath, Class<T> type) {
        this.filePath = filePath;
        this.type = type;
        
        // Create the file if it doesn't exist
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
                saveAll(new ArrayList<>());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create file: " + filePath, e);
            }
        }
    }
    
    /**
     * Get all objects from the file
     * @return List of objects
     */
    public List<T> getAll() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                return ((List<?>) obj).stream()
                        .filter(type::isInstance)
                        .map(type::cast)
                        .collect(Collectors.toList());
            }
        } catch (EOFException e) {
            // Empty file
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            // If there's an error, return an empty list
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }
    
    /**
     * Save all objects to the file
     * @param objects List of objects to save
     */
    public void saveAll(List<T> objects) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(objects);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save to file: " + filePath, e);
        }
    }
    
    /**
     * Find an object by a predicate
     * @param predicate Predicate to match
     * @return Optional containing the object if found
     */
    public Optional<T> findOne(Predicate<T> predicate) {
        return getAll().stream()
                .filter(predicate)
                .findFirst();
    }
    
    /**
     * Find all objects matching a predicate
     * @param predicate Predicate to match
     * @return List of matching objects
     */
    public List<T> findAll(Predicate<T> predicate) {
        return getAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    /**
     * Save an object
     * @param object Object to save
     * @return Saved object
     */
    public T save(T object) {
        List<T> objects = getAll();
        objects.add(object);
        saveAll(objects);
        return object;
    }
    
    /**
     * Update an object
     * @param predicate Predicate to match the object to update
     * @param updatedObject Updated object
     * @return Optional containing the updated object if found
     */
    public Optional<T> update(Predicate<T> predicate, T updatedObject) {
        List<T> objects = getAll();
        int index = -1;
        
        for (int i = 0; i < objects.size(); i++) {
            if (predicate.test(objects.get(i))) {
                index = i;
                break;
            }
        }
        
        if (index != -1) {
            objects.set(index, updatedObject);
            saveAll(objects);
            return Optional.of(updatedObject);
        }
        
        return Optional.empty();
    }
    
    /**
     * Delete an object
     * @param predicate Predicate to match the object to delete
     * @return true if deleted, false otherwise
     */
    public boolean delete(Predicate<T> predicate) {
        List<T> objects = getAll();
        List<T> newObjects = objects.stream()
                .filter(predicate.negate())
                .collect(Collectors.toList());
        
        if (newObjects.size() < objects.size()) {
            saveAll(newObjects);
            return true;
        }
        
        return false;
    }
}
