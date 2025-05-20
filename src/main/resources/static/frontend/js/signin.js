document.querySelector('form').addEventListener('submit', function (event) {
    event.preventDefault();

    const user = {
        name: document.getElementById('name').value.trim(),
        email: document.getElementById('email').value.trim(),
        phone: document.getElementById('phone').value.trim(),
        password: document.getElementById('password').value.trim(),
        dob: document.getElementById('dob').value,
        role: "user"
    };

    fetch('http://localhost:8080/api/users/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text); });
            }
            return response.json();
        })
        .then(data => {
            alert("User registered successfully!");
            sessionStorage.setItem("currentUser", JSON.stringify(data));
            window.location.href = "homepage.html";
        })

        .catch(error => {
            console.error('Sign-up failed:', error);
            alert("Sign-up failed: " + error.message);
        });
});
