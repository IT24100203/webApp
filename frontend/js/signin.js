function validateForm() {
    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const phone = document.getElementById("phone").value.trim();
    const password = document.getElementById("password").value.trim();
    const dob = document.getElementById("dob").value;

    if (!name || !email || !phone || !password || !dob) {
        alert("All fields are required!");
        return false;
    }

    const phoneRegex = /^[0-9]{10}$/;
    if (!phoneRegex.test(phone)) {
        alert("Enter a valid 10-digit phone number.");
        return false;
    }

    const users = JSON.parse(localStorage.getItem("users")) || [];

    const existingUser = users.find(user => user.email === email);
    if (existingUser) {
        alert("Email is already registered!");
        return false;
    }

    const newUser = {
        name,
        email,
        phone,
        password,
        dob
    };

    users.push(newUser);
    localStorage.setItem("users", JSON.stringify(users));

    alert("Account created successfully!");
    window.location.href = "login.html";
    return false;
}
