package com.AdminSystem.reservation.controller;

import com.AdminSystem.reservation.dto.ReportDto;
import com.AdminSystem.reservation.service.AdminDashboardService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    private final AdminDashboardService service;

    public AdminDashboardController(AdminDashboardService service) {
        this.service = service;
    }

    @GetMapping("/report")
    public ReportDto getReport(@RequestParam String date) {
        return service.generateReport(LocalDate.parse(date));
    }

    @DeleteMapping("/purge")
    public void purgeOldReservations() {
        service.purgeOldReservations();
    }
}
