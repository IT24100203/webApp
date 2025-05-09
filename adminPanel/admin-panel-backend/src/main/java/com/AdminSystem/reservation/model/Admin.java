package com.AdminSystem.reservation.model;

public class Admin extends User {
    public Admin() { super(); }

    public Admin(String username, String password) {
        super(username, password);
    }

    public static Admin fromLine(String line) {
        String[] parts = line.split(",");
        return new Admin(parts[0], parts[1]);
    }

    public String toLine() {
        return getUsername() + "," + getPassword();
    }
}
