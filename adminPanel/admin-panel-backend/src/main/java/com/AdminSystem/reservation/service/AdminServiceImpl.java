package com.AdminSystem.reservation.service;

import com.AdminSystem.reservation.dto.ReportDto;
import com.AdminSystem.reservation.model.Reservation;
import com.AdminSystem.reservation.model.Table;
import com.AdminSystem.reservation.model.Admin;
import com.AdminSystem.reservation.util.AuditLogger;
import com.AdminSystem.reservation.util.FileHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private static final String TABLES_FILE = "data/tables.txt";
    private static final String RES_FILE = "data/reservations.txt";
    private static final String ADMINS_FILE = "data/admins.txt";

    @Override
    public boolean authenticate(String username, String password) {
        List<Admin> admins = FileHelper.readList(ADMINS_FILE, Admin::fromLine);

        System.out.println("Checking credentials for: " + username + " / " + password);
        admins.forEach(a -> System.out.println("Loaded: " + a.getUsername() + " / " + a.getPassword()));

        return admins.stream()
                .anyMatch(a -> a.getUsername().equals(username) && a.getPassword().equals(password));
    }

    @Override
    public void addTable(Table t) {
        List<Table> tables = listTables();
        if (tables == null) tables = new ArrayList<>();

        int nextId = tables.stream().mapToInt(Table::getId).max().orElse(0) + 1;
        t.setId(nextId);
        tables.add(t);

        FileHelper.writeList(TABLES_FILE, tables, Table::toLine);
        AuditLogger.getInstance().log("Added table " + t.getId());
    }

    @Override
    public void removeTable(int id) {
        List<Table> tables = listTables();
        if (tables == null) tables = new ArrayList<>();

        tables = tables.stream()
                .filter(t -> t.getId() != id)
                .collect(Collectors.toList());

        FileHelper.writeList(TABLES_FILE, tables, Table::toLine);
        AuditLogger.getInstance().log("Removed table " + id);
    }

    @Override
    public List<Table> listTables() {
        return FileHelper.readList(TABLES_FILE, Table::fromLine);
    }

    @Override
    public List<Reservation> listAllReservations() {
        return FileHelper.readList(RES_FILE, Reservation::fromLine);
    }

    @Override
    public void overrideReservation(Reservation r) {
        List<Reservation> all = listAllReservations();
        if (all == null) all = new ArrayList<>();

        List<Reservation> updated = all.stream()
                .map(x -> x.getId() == r.getId() ? r : x)
                .collect(Collectors.toList());

        FileHelper.writeList(RES_FILE, updated, Reservation::toLine);
        AuditLogger.getInstance().log("Overrode reservation " + r.getId());
    }

    @Override
    public void purgeOldReservations() {
        List<Reservation> all = listAllReservations();
        if (all == null) return;

        LocalDate today = LocalDate.now();

        List<Reservation> filtered = all.stream()
                .filter(r -> r.getDate().isAfter(today))
                .collect(Collectors.toList());

        FileHelper.writeList(RES_FILE, filtered, Reservation::toLine);
        AuditLogger.getInstance().log("Purged old reservations");
    }

    @Override
    public ReportDto generateReport(LocalDate date) {
        List<Reservation> all = listAllReservations();
        List<Table> tables = listTables();

        long totalReservations = all.stream()
                .filter(r -> r.getDate().equals(date))
                .count();

        int totalTables = tables.size();
        double utilizationRate = totalTables > 0 ? (double) totalReservations / totalTables * 100 : 0;

        return new ReportDto(date, (int) totalReservations, totalTables, utilizationRate);
    }
}
