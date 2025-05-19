package lk.re_es.webApp.model;

import java.util.Date;

public class Reservation extends BaseEntity{
    private String name;
    private String phoneNumber;
    private String email;
    private int numOfPeople;
    private Date date;

    public Reservation(String name, String phoneNumber, String email, int numOfPeople, Date date) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.numOfPeople = numOfPeople;
        this.date = date;
    }
}