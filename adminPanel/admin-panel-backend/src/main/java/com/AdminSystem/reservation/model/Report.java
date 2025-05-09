package com.AdminSystem.reservation.model;

import java.time.LocalDate;

public class Report {
    private LocalDate date;
    private long totalReservations;
    private int totalTables;
    private double utilizationRate;

    public Report() {}

    public Report(LocalDate date, long totalReservations, int totalTables, double utilizationRate) {
        this.date = date;
        this.totalReservations = totalReservations;
        this.totalTables = totalTables;
        this.utilizationRate = utilizationRate;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(long totalReservations) {
        this.totalReservations = totalReservations;
    }

    public int getTotalTables() {
        return totalTables;
    }

    public void setTotalTables(int totalTables) {
        this.totalTables = totalTables;
    }

    public double getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(double utilizationRate) {
        this.utilizationRate = utilizationRate;
    }
}
