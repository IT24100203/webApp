package com.AdminSystem.reservation.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditLogger {
    private static AuditLogger instance;
    private final String logFile = "data/audit.txt";

    private AuditLogger() {}

    public static synchronized AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }

    public void log(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(logFile, true))) {
            out.println(LocalDateTime.now() + ": " + message);
        } catch (IOException e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }
}
