let allReservations = [];

document.addEventListener("DOMContentLoaded", () => {
    // Check if user is logged in
    const currentUser = JSON.parse(sessionStorage.getItem("currentUser"));
    if (!currentUser) {
        window.location.href = "login.html";
        return;
    }

    loadReservations();
    
    const searchInput = document.getElementById("searchReservation");
    if (searchInput) {
        searchInput.addEventListener("input", filterReservations);
    }

    // Add click handlers for navigation buttons
    document.getElementById('reservationBtn').addEventListener('click', function() {
        showSection('reservation');
        loadReservations();
    });

    document.getElementById('userManagementBtn').addEventListener('click', function() {
        showSection('userManagement');
    });
});

function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll('.content-section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Show selected section
    document.getElementById(sectionId + 'Section').classList.add('active');
    
    // Update active button
    document.querySelectorAll('.nav-button').forEach(button => {
        button.classList.remove('active');
    });
    document.getElementById(sectionId + 'Btn').classList.add('active');
}

function loadReservations() {
    fetch("http://localhost:8080/api/reservations")
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to load reservations');
            }
            return response.json();
        })
        .then(reservations => {
            allReservations = reservations;
            renderReservationTable(reservations);
        })
        .catch(error => {
            console.error('Error loading reservations:', error);
            document.getElementById('reservationTable').innerHTML = 
                '<tr><td colspan="9" class="error-message">Failed to load reservations. Please try again later.</td></tr>';
        });
}

function renderReservationTable(reservations) {
    const tableBody = document.getElementById('reservationTable');
    tableBody.innerHTML = '';

    if (reservations.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="9" class="no-reservations">No reservations found.</td></tr>';
        return;
    }

    reservations.forEach(reservation => {
        const row = document.createElement('tr');
        const statusClass = reservation.status.toLowerCase();
        
        row.innerHTML = `
            <td>${reservation.id}</td>
            <td>${reservation.name}</td>
            <td>${reservation.email}</td>
            <td>${reservation.phone}</td>
            <td>${formatDate(reservation.date)}</td>
            <td>${formatTime(reservation.time)}</td>
            <td>${reservation.guests}</td>
            <td><span class="status-badge ${statusClass}">${reservation.status}</span></td>
            <td class="actions">
                ${getActionButtons(reservation)}
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function getActionButtons(reservation) {
    const status = reservation.status.toUpperCase();
    let buttons = '';

    // Only show confirm button if status is PENDING
    if (status === 'PENDING') {
        buttons += `<button onclick="updateReservationStatus('${reservation.id}', 'CONFIRMED')" class="action-btn confirm" title="Confirm">✓</button>`;
    }

    // Only show cancel button if status is not CANCELLED
    if (status !== 'CANCELLED') {
        buttons += `<button onclick="updateReservationStatus('${reservation.id}', 'CANCELLED')" class="action-btn cancel" title="Cancel">✕</button>`;
    }

    // Always show delete button
    buttons += `<button onclick="deleteReservation('${reservation.id}')" class="action-btn delete" title="Delete">🗑️</button>`;

    return buttons;
}

function filterReservations(event) {
    const searchTerm = event.target.value.toLowerCase();
    const filteredReservations = allReservations.filter(reservation => {
        return (
            reservation.name.toLowerCase().includes(searchTerm) ||
            reservation.email.toLowerCase().includes(searchTerm) ||
            reservation.phone.toLowerCase().includes(searchTerm) ||
            formatDate(reservation.date).toLowerCase().includes(searchTerm) ||
            formatTime(reservation.time).toLowerCase().includes(searchTerm) ||
            reservation.status.toLowerCase().includes(searchTerm)
        );
    });
    renderReservationTable(filteredReservations);
}

function updateReservationStatus(id, newStatus) {
    const reservation = allReservations.find(r => r.id === id);
    if (!reservation) return;

    // Show confirmation dialog
    const action = newStatus === 'CONFIRMED' ? 'confirm' : 'cancel';
    if (!confirm(`Are you sure you want to ${action} this reservation?`)) {
        return;
    }

    fetch(`http://localhost:8080/api/reservations/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            ...reservation,
            status: newStatus
        }),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to update reservation');
        }
        return response.json();
    })
    .then(() => {
        showNotification(`Reservation ${action}ed successfully!`);
        loadReservations();
    })
    .catch(error => {
        console.error('Error updating reservation:', error);
        showNotification('Failed to update reservation status', 'error');
    });
}

function deleteReservation(id) {
    if (!confirm('Are you sure you want to delete this reservation? This action cannot be undone.')) {
        return;
    }

    fetch(`http://localhost:8080/api/reservations/${id}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to delete reservation');
        }
        return response.json();
    })
    .then(() => {
        showNotification('Reservation deleted successfully!');
        loadReservations();
    })
    .catch(error => {
        console.error('Error deleting reservation:', error);
        showNotification('Failed to delete reservation', 'error');
    });
}

function showNotification(message, type = 'success') {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;

    // Add to document
    document.body.appendChild(notification);

    // Show notification
    setTimeout(() => {
        notification.classList.add('show');
    }, 100);

    // Remove after 3 seconds
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    }, 3000);
}

function formatDate(dateString) {
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return new Date(dateString).toLocaleDateString(undefined, options);
}

function formatTime(timeString) {
    return new Date(`2000-01-01T${timeString}`).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
} 