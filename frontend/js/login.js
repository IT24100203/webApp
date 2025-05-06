function validateForm() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    const users = [
        { name: "Admin", email: "bimandibosilu@gmail.com", password: "admin123", role: "admin" },
        { name: "User1", email: "user@example.com", password: "user1234", role: "admin" },
        { name: "User2", email: "user@example.com", password: "user12345", role: "admin" },
        { name: "User3", email: "user@example.com", password: "user123456", role: "admin" },
        { name: "User4", email: "user@example.com", password: "user1234567", role: "admin" },
        { name: "User5", email: "user@example.com", password: "user12345678", role: "admin" }
    ];

    const user = users.find(u => u.email === email && u.password === password);

    if (!user) {
        alert("Invalid email or password.");
        return false;
    }

    sessionStorage.setItem("currentUser", JSON.stringify(user));
    alert("Login successful!");

    if (user.role === "admin") {
        showAdminButton();
    }

    return false;
}

function showAdminButton() {
    const adminBtn = document.createElement("a");
    adminBtn.href = "admin-users.html";
    adminBtn.textContent = "Manage Users";
    adminBtn.className = "admin-btn";
    document.getElementById("admin-section").appendChild(adminBtn);
}

const loggedUser = JSON.parse(sessionStorage.getItem("currentUser"));
if (loggedUser && loggedUser.role === "admin") {
    showAdminButton();
}
