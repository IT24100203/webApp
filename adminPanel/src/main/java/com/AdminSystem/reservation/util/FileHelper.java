package com.AdminSystem.reservation.util;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileHelper {

    public static <T> List<T> readList(String file, Function<String, T> parser) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            return br.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .map(parser)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Failed to read file: " + file + " - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static <T> void writeList(String file, List<T> list, Function<T, String> toLine) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            list.stream()
                    .map(toLine)
                    .forEach(pw::println);
        } catch (IOException e) {
            System.err.println("Failed to write file: " + file + " - " + e.getMessage());
        }
    }
}
