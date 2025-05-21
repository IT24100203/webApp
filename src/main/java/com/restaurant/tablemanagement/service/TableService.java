package com.restaurant.tablemanagement.service;

import com.restaurant.tablemanagement.model.Table;
import com.restaurant.tablemanagement.repository.TableFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TableService {
    
    private final TableFileRepository tableRepository;
    
    @Autowired
    public TableService(TableFileRepository tableRepository) {
        this.tableRepository = tableRepository;
    }
    
    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }
    
    public Optional<Table> getTableById(Long id) {
        return tableRepository.findById(id);
    }
    
    public Table createTable(Table table) {
        return tableRepository.save(table);
    }
    
    public Optional<Table> updateTable(Long id, Table tableDetails) {
        return tableRepository.findById(id)
                .map(existingTable -> {
                    existingTable.setTableNumber(tableDetails.getTableNumber());
                    existingTable.setCapacity(tableDetails.getCapacity());
                    existingTable.setStatus(tableDetails.getStatus());
                    existingTable.setLocation(tableDetails.getLocation());
                    return tableRepository.save(existingTable);
                });
    }
    
    public boolean deleteTable(Long id) {
        if (tableRepository.findById(id).isPresent()) {
            tableRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Method to sort tables by capacity using QuickSort
    public List<Table> getTablesSortedByCapacity() {
        List<Table> tables = tableRepository.findAll();
        quickSortByCapacity(tables, 0, tables.size() - 1);
        return tables;
    }
    
    // QuickSort implementation for sorting tables by capacity
    private void quickSortByCapacity(List<Table> tables, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(tables, low, high);
            quickSortByCapacity(tables, low, pivotIndex - 1);
            quickSortByCapacity(tables, pivotIndex + 1, high);
        }
    }
    
    private int partition(List<Table> tables, int low, int high) {
        int pivot = tables.get(high).getCapacity();
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (tables.get(j).getCapacity() <= pivot) {
                i++;
                // Swap tables[i] and tables[j]
                Table temp = tables.get(i);
                tables.set(i, tables.get(j));
                tables.set(j, temp);
            }
        }
        
        // Swap tables[i+1] and tables[high] (pivot)
        Table temp = tables.get(i + 1);
        tables.set(i + 1, tables.get(high));
        tables.set(high, temp);
        
        return i + 1;
    }
}
