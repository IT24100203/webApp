package com.AdminSystem.reservation.util;

import java.io.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileHelper {
    public static <T> List<T> readList(String file, Function<String, T> parser) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().filter(l -> !l.trim().isEmpty()).map(parser).collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }

    public static <T> void writeList(String file, List<T> list, Function<T, String> toLine) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            list.stream().map(toLine).forEach(writer::println);
        } catch (IOException e) {
            System.err.println("Write failed: " + e.getMessage());
        }
    }
}
