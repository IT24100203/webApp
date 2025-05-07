package com.AdminSystem.reservation.dto;

import java.time.LocalDate;

public class ReportDto {
    private LocalDate date;
    private int totalReservations;
    private int totalTables;
    private double utilizationRate;

    public ReportDto(LocalDate date, int totalReservations, int totalTables, double utilizationRate) {
        this.date = date;
        this.totalReservations = totalReservations;
        this.totalTables = totalTables;
        this.utilizationRate = utilizationRate;
    }

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
}
