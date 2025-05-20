package lk.re_es.webApp.model;

public class Person extends BaseEntity {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;

    public Person() {
        super();
    }

    public Person(Long id, String name, String email, String phone, String password, String role, String status) {
        super(id, status);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    // Constructor for basic user creation
    public Person(Long id, String name, String email, String phone, String password) {
        super(id, "ACTIVE");
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = "USER";
    }

    // Copy constructor
    public Person(Person person) {
        super(person.getId(), person.getStatus());
        this.name = person.getName();
        this.email = person.getEmail();
        this.phone = person.getPhone();
        this.password = person.getPassword();
        this.role = person.getRole();
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
    public String toString() {
        return "Person{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", status='" + getStatus() + '\'' +
                ", uniqueId=" + getUniqueId() +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    // Polymorphism
    public String getDetails() {
        return "Name: " + name + ", Email: " + email;
    }
}