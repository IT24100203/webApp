package com.AdminSystem.reservation.model;

public class Table {
    private int id;
    private int seats;

    public Table() {}

    public Table(int id, int seats) {
        this.id = id;
        this.seats = seats;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    // File I/O conversion
    public static Table fromLine(String line) {
        String[] parts = line.split(",");
        return new Table(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public String toLine() {
        return id + "," + seats;
    }
}
