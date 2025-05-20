package lk.re_es.webApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Person {
    private String password;
    private String role;
    private String status;

    public User() {
        super();
    }

    public User(Long id, String name, String email, String phone, String password, String role, String status) {
        super(id, name, email, phone, password, role, status);
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(Long id, String name, String email, String phone, String password) {
        super(id, name, email, phone, password);
    }

    // Copy constructor
    public User(User user) {
        super(user);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getDetails() {
        return "User Details - Name: " + getName() + ", Email: " + getEmail() + ", Role: " + getRole();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}