package lk.re_es.webApp.model;

public class Person extends BaseEntity {
    private String name;
    private String email;
    private String phone;
    private String dob;

    public Person() {
        super();
    }

    public Person(Long id, String name, String email, String phone, String dob) {
        super(id);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
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

    // Polymorphism
    public String getDetails() {
        return "Name: " + name + ", Email: " + email;
    }
}