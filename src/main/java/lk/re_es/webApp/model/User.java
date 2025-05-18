package lk.re_es.webApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends Person {
    private String password;
    private String role;

    public User() {
        super();
    }

    public User(Long id, String name, String email, String phone, String dob, String password, String role) {
        super(id, name, email, phone, dob);
        this.password = password;
        this.role = role;
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

    @Override
    public String getDetails() {
        return "Name: " + getName() + ", Email: " + getEmail();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", dob='" + getDob() + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}