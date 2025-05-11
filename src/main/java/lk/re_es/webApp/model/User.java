package lk.re_es.webApp.model;

public class User {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String dob;
    private String role;


    public User() {
    }

    public User(String name, String email, String password, String phone, String dob, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dob = dob;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
