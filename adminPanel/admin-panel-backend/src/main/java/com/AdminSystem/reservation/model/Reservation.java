package com.AdminSystem.reservation.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private int id;
    private String customerName;
    private int tableId;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public Reservation() {}

    public Reservation(int id, String customerName, int tableId, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.customerName = customerName;
        this.tableId = tableId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public int getTableId() { return tableId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setCustomerName(String name) { this.customerName = name; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTime(LocalTime time) { this.time = time; }
    public void setStatus(String status) { this.status = status; }

    // File IO helpers
    public static Reservation fromLine(String line) {
        String[] parts = line.split("\\|");
        return new Reservation(
                Integer.parseInt(parts[0]),
                parts[1],
                Integer.parseInt(parts[3]),
                LocalDate.parse(parts[2].split("T")[0]),
                LocalTime.parse(parts[2].split("T")[1]),
                parts[4]
        );
    }

    public String toLine() {
        return id + "|" + customerName + "|" + date + "T" + time + "|" + tableId + "|" + status;
    }
}
