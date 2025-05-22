package lk.re_es.webApp.model;

import java.time.LocalDateTime;

public class Reservation extends BaseEntity {
    private String userId;
    private String name;
    private String phone;
    private String email;
    private int guests;
    private LocalDateTime date;

    public Reservation() {
        super();
    }

    public Reservation(Long id, String userId, String name, String phone, String email, int guests, LocalDateTime date, String status) {
        super(id, status);
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.guests = guests;
        this.date = date;
    }

    // Copy constructor
    public Reservation(Reservation reservation) {
        super(reservation.getId(), reservation.getStatus());
        this.userId = reservation.getUserId();
        this.name = reservation.getName();
        this.phone = reservation.getPhone();
        this.email = reservation.getEmail();
        this.guests = reservation.getGuests();
        this.date = reservation.getDate();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + getId() +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", guests=" + guests +
                ", date=" + date +
                ", status='" + getStatus() + '\'' +
                ", uniqueId=" + getUniqueId() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}