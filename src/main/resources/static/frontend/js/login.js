document.getElementById('login-form').addEventListener('submit', function(event) {
    console.log('login triggered')
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
                sessionStorage.setItem('currentUser', JSON.stringify(data));

                if (data.role === "admin") {
                    window.location.href = '/frontend/html/landing-page-admin.html';
                } else {
                    window.location.href = '/frontend/html/landing-page-user.html';
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