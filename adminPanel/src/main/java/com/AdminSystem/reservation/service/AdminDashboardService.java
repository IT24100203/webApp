package com.AdminSystem.reservation.service;

import com.AdminSystem.reservation.dto.ReportDto;

import java.time.LocalDate;

public interface AdminDashboardService {
    ReportDto generateReport(LocalDate date);
    void purgeOldReservations();
}
