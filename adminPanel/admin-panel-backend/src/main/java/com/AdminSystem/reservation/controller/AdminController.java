package com.AdminSystem.reservation.controller;

import com.AdminSystem.reservation.dto.*;
import com.AdminSystem.reservation.model.Admin;
import com.AdminSystem.reservation.model.Reservation;
import com.AdminSystem.reservation.service.AdminService;
import com.AdminSystem.reservation.util.AuditLogger;
import com.AdminSystem.reservation.util.FileHelper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public AuthToken login(@RequestBody LoginRequest request) {
        List<Admin> admins = FileHelper.readList("data/admins.txt", Admin::fromLine);

        boolean valid = admins.stream()
                .anyMatch(a -> a.getUsername().equals(request.getUsername())
                        && a.getPassword().equals(request.getPassword()));

        if (valid) {
            AuditLogger.getInstance().log("Login successful for " + request.getUsername());
            return new AuthToken("token");
        }

        AuditLogger.getInstance().log("Login failed for " + request.getUsername());
        throw new RuntimeException("Invalid credentials");
    }


    // ---------- TABLE CRUD ----------
    @PostMapping("/tables")
    public void addTable(@RequestBody TableDto dto) {
        adminService.addTable(dto.toModel());
        AuditLogger.getInstance().log("Added table with seats: " + dto.getSeats());
    }

    @DeleteMapping("/tables/{id}")
    public void removeTable(@PathVariable int id) {
        adminService.removeTable(id);
        AuditLogger.getInstance().log("Removed table with ID: " + id);
    }

    @GetMapping("/tables")
    public List<TableDto> getTables() {
        return adminService.listTables().stream()
                .map(TableDto::fromModel)
                .toList();
    }

    // ---------- RESERVATIONS ----------
    @GetMapping("/reservations")
    public List<ReservationDto> getReservations(@RequestParam(defaultValue = "false") boolean sorted) {
        List<Reservation> list = adminService.listAllReservations();
        if (sorted) {
            list.sort((a, b) -> a.getTime().compareTo(b.getTime()));
        }
        return list.stream().map(ReservationDto::fromModel).toList();
    }

    @PutMapping("/reservations")
    public void overrideReservation(@RequestBody ReservationDto dto) {
        adminService.overrideReservation(dto.toModel());
        AuditLogger.getInstance().log("Overridden reservation: " + dto.getId() + " -> " + dto.getStatus());
    }

    @DeleteMapping("/reservations/purge")
    public void purgeOldReservations() {
        adminService.purgeOldReservations();
        AuditLogger.getInstance().log("Purged old reservations");
    }

    // ---------- REPORTS ----------
    @GetMapping("/reports")
    public ReportDto getReport(@RequestParam String date) {
        LocalDate parsed = LocalDate.parse(date);
        return adminService.generateReport(parsed); // ✅ no stream()
    }
}
