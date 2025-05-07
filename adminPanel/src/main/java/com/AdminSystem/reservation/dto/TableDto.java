package com.AdminSystem.reservation.dto;

import com.AdminSystem.reservation.model.Table;

public class TableDto {
    private int id;
    private int seats;

    public TableDto() {
    }

    public TableDto(int id, int seats) {
        this.id = id;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public static TableDto fromModel(Table t) {
        return new TableDto(t.getId(), t.getSeats());
    }

    public Table toModel() {
        return new Table(id, seats);
    }
}
