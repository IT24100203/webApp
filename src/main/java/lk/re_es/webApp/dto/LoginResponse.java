package lk.re_es.webApp.dto;

public class LoginResponse {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String dob;
    private String role;

    public LoginResponse(String name, String email, String phone,String password,String dob, String role) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.dob = dob;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }

    public String getRole() {
        return role;
    }
}
