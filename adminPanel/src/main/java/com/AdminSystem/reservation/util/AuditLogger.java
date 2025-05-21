package com.AdminSystem.reservation.util;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class AuditLogger {
    private static final String file = "data/audit.txt";

    private AuditLogger() {
    }

    public static void log(String msg) {
        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            out.println(LocalDateTime.now() + ": " + msg);
        } catch (Exception e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }
}
