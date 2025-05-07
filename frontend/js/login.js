function validateForm() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    const users = [
        { name: "Admin", email: "bima@gmail.com", password: "bima123", role: "admin" },
        { name: "User1", email: "thanu@gmail.com", password: "thanu123", role: "admin" },
        { name: "User2", email: "kush@gmail.com", password: "kush123", role: "admin" },
        { name: "User3", email: "tharu@gmail.com", password: "tharu123", role: "admin" },
        { name: "User4", email: "kesh@gmail.com", password: "kesh123", role: "admin" },
        { name: "User5", email: "abeyy@gmail.com", password: "abeyy123", role: "admin" }
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

function handleLogin() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // Example condition — replace with actual logic
    if (email === "admin@example.com" && password === "1234") {
        document.getElementById('login-message').style.display = "block";

        //optional
        setTimeout(() => {
            window.location.href = "homepage.html";
        }, 1500);
    } else {
        alert("Invalid login credentials");
    }
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
