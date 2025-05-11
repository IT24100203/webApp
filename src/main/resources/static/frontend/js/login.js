document.getElementById('login-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value.trim();

    const loginData = {
        email: email,
        password: password
    };

    fetch('http://localhost:8080/api/users/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text); });
            }
            return response.json();
        })
        .then(data => {
            console.log("Logged in user data:", data);

            if (data && data.role) {
                // If data and role exist, proceed with login
                sessionStorage.setItem('currentUser', JSON.stringify(data));

                if (data.role === "admin") {
                    showAdminButton();
                    alert('Login successful! Redirecting to admin page.');
                    window.location.href = 'admin-users.html';
                } else {
                    alert('Login successful! Redirecting to homepage.');
                    window.location.href = 'homepage.html';
                }
            } else {
                throw new Error("Invalid user data received from API.");
            }
        })
        .catch(error => {
            console.error('Login error:', error);
            alert('Login failed: ' + error.message);
        });
});

function showAdminButton() {
    const adminBtn = document.createElement("a");
    adminBtn.href = "admin-users.html";
    adminBtn.textContent = "Manage Users";
    adminBtn.className = "admin-btn";
    document.getElementById("admin-section").appendChild(adminBtn);
}

// Show admin button if already logged in
const loggedUser = JSON.parse(sessionStorage.getItem("currentUser"));
if (loggedUser && loggedUser.role === "admin") {
    showAdminButton();
}
