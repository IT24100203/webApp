document.addEventListener("DOMContentLoaded", () => {
    // Add logout functionality
    document.getElementById('logoutBtn').addEventListener('click', function(e) {
        e.preventDefault();
        // Clear the session storage
        sessionStorage.removeItem('currentUser');
        // Redirect to login page
        window.location.href = 'login.html';
    });
});

function submitReservation() {
    const currentUser = JSON.parse(sessionStorage.getItem("currentUser"));
    if (!currentUser) {
        window.location.href = "login.html";
        return;
    }

    const name = document.getElementById("reservationName").value;
    const phone = document.getElementById("reservationPhone").value;
    const email = document.getElementById("reservationEmail").value;
    const guests = document.getElementById("reservationGuests").value;
    const date = document.getElementById("reservationDate").value;

    if (!name || !phone || !email || !guests || !date) {
        alert("Please fill in all fields");
        return;
    }

    const newReservation = {
        userId: currentUser.email,
        name,
        phone,
        email,
        guests: parseInt(guests),
        date,
        status: "PENDING"
    };

    fetch("http://localhost:8080/api/reservations", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(newReservation),
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(data => {
            alert("Reservation submitted successfully!");
            document.getElementById("reservationForm").reset();
        })
        .catch(error => {
            console.error("Error creating reservation:", error);
            alert("Failed to create reservation. Please try again.");
        });
} 