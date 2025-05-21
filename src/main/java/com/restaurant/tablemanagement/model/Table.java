package com.restaurant.tablemanagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table implements Serializable {
    private Long id;
    private String tableNumber;
    private int capacity;
    private TableStatus status;
    private String location; // e.g., "Indoor", "Outdoor", "Bar"
    
    // Enum for table status
    public enum TableStatus {
        AVAILABLE,
        RESERVED,
        OCCUPIED,
        MAINTENANCE
    }
}
