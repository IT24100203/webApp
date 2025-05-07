package com.AdminSystem.reservation.service;

import com.AdminSystem.reservation.dto.ReportDto;
import com.AdminSystem.reservation.model.Reservation;
import com.AdminSystem.reservation.model.Table;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {
    boolean authenticate(String username, String password);        // <-- add this
    void addTable(Table t);
    void removeTable(int id);
    List<Table> listTables();
    List<Reservation> listAllReservations();
    void overrideReservation(Reservation r);
    void purgeOldReservations();
    ReportDto generateReport(LocalDate date);
}
