package com.AdminSystem.reservation.service;

import com.AdminSystem.reservation.dto.ReportDto;
import com.AdminSystem.reservation.model.Reservation;
import com.AdminSystem.reservation.model.Table;
import com.AdminSystem.reservation.util.FileHelper;
import com.AdminSystem.reservation.util.AuditLogger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private static final String TABLES_FILE = "data/tables.txt";
    private static final String RES_FILE = "data/reservations.txt";

    @Override
    public ReportDto generateReport(LocalDate date) {
        List<Reservation> reservations = FileHelper.readList(RES_FILE, Reservation::fromLine);
        List<Table> tables = FileHelper.readList(TABLES_FILE, Table::fromLine);

        List<Reservation> onDate = reservations.stream()
                .filter(r -> r.getDate().equals(date))
                .toList();

        int pending = (int) onDate.stream().filter(r -> r.getStatus().equalsIgnoreCase("PENDING")).count();
        int seated = (int) onDate.stream().filter(r -> r.getStatus().equalsIgnoreCase("SEATED")).count();
        int cancelled = (int) onDate.stream().filter(r -> r.getStatus().equalsIgnoreCase("CANCELLED")).count();

        int totalTables = tables.size();
        double rate = totalTables > 0 ? (double) onDate.size() / totalTables * 100 : 0;

        AuditLogger.log("Generated detailed report for " + date);

        return new ReportDto(
                date,
                onDate.size(),
                totalTables,
                rate,
                pending,
                seated,
                cancelled
        );
    }

    @Override
    public void purgeOldReservations() {
        List<Reservation> reservations = FileHelper.readList(RES_FILE, Reservation::fromLine);
        LocalDate today = LocalDate.now();

        List<Reservation> filtered = reservations.stream()
                .filter(r -> r.getDate().isAfter(today))
                .toList();

        FileHelper.writeList(RES_FILE, filtered, Reservation::toLine);
        AuditLogger.log("Purged old reservations");
    }
}
