package com.AdminSystem.reservation.dto;

import com.AdminSystem.reservation.model.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDto {
    private int id;
    private String name;
    private LocalDate date;
    private LocalTime time;
    private int tableId;
    private String status;

    public ReservationDto() { }

    public ReservationDto(int id, String name, LocalDate date, LocalTime time, int tableId, String status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.tableId = tableId;
        this.status = status;
    }

    // Convert from model to DTO
    public static ReservationDto fromModel(Reservation r) {
        return new ReservationDto(
                r.getId(),
                r.getCustomerName(),
                r.getDate(),
                r.getTime(),
                r.getTableId(),
                r.getStatus()
        );
    }

    // Convert from DTO to model
    public Reservation toModel() {
        return new Reservation(id, name, tableId, date, time, status);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
