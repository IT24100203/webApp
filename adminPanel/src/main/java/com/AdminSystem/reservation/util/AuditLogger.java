package com.AdminSystem.reservation.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditLogger {
    private static AuditLogger instance;
    private final String file = "data/audit.txt";

    private AuditLogger() {}

    public static synchronized AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }

    public void log(String msg) {
        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            out.println(LocalDateTime.now() + ": " + msg);
        } catch (Exception e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }
}
