package com.restaurant.tablemanagement.repository;

import com.restaurant.tablemanagement.model.Table;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TableFileRepository {
    private static final String DATA_FILE = "tables.dat";
    private final AtomicLong idCounter = new AtomicLong(1);

    public TableFileRepository() {
        // Initialize the repository and load the counter
        List<Table> tables = loadTables();
        if (!tables.isEmpty()) {
            long maxId = tables.stream()
                    .mapToLong(Table::getId)
                    .max()
                    .orElse(0);
            idCounter.set(maxId + 1);
        }
    }

    public List<Table> findAll() {
        return loadTables();
    }

    public Optional<Table> findById(Long id) {
        return loadTables().stream()
                .filter(table -> table.getId().equals(id))
                .findFirst();
    }

    public Table save(Table table) {
        List<Table> tables = loadTables();
        
        if (table.getId() == null) {
            // New table
            table.setId(idCounter.getAndIncrement());
            tables.add(table);
        } else {
            // Update existing table
            tables.removeIf(t -> t.getId().equals(table.getId()));
            tables.add(table);
        }
        
        saveTables(tables);
        return table;
    }

    public void deleteById(Long id) {
        List<Table> tables = loadTables();
        tables.removeIf(table -> table.getId().equals(id));
        saveTables(tables);
    }

    private List<Table> loadTables() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Table>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tables: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveTables(List<Table> tables) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(tables);
        } catch (IOException e) {
            System.err.println("Error saving tables: " + e.getMessage());
        }
    }
}
