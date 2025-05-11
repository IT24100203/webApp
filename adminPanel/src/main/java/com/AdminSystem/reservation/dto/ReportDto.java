package com.AdminSystem.reservation.dto;

import java.time.LocalDate;

public class ReportDto {
    private LocalDate date;
    private int totalReservations;
    private int totalTables;
    private double utilizationRate;
    private int pendingCount;
    private int seatedCount;
    private int cancelledCount;

    public ReportDto(LocalDate date, int totalReservations, int totalTables, double utilizationRate,
                     int pendingCount, int seatedCount, int cancelledCount) {
        this.date = date;
        this.totalReservations = totalReservations;
        this.totalTables = totalTables;
        this.utilizationRate = utilizationRate;
        this.pendingCount = pendingCount;
        this.seatedCount = seatedCount;
        this.cancelledCount = cancelledCount;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public int getTotalReservations() {
        return totalReservations;
    }

    public int getTotalTables() {
        return totalTables;
    }

    public double getUtilizationRate() {
        return utilizationRate;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public int getSeatedCount() {
        return seatedCount;
    }

    public int getCancelledCount() {
        return cancelledCount;
    }
}
