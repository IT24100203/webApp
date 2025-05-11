document.addEventListener('DOMContentLoaded', () => {
    loadUserProfile();
});

function loadUserProfile() {
    const currentUser = JSON.parse(sessionStorage.getItem("currentUser"));

    if (!currentUser) {
        alert("No user is currently logged in.");
        window.location.href = "login.html";
        return;
    }

    document.getElementById('name').value = currentUser.name;
    document.getElementById('email').value = currentUser.email;
}

function enableEdit(id) {
    const input = document.getElementById(id);
    input.removeAttribute('readonly');
    input.focus();
}

function clearField(id) {
    const input = document.getElementById(id);
    input.value = '';
}

function saveProfile() {
    const currentUser = JSON.parse(sessionStorage.getItem("currentUser"));

    if (!currentUser) {
        alert("No user is currently logged in.");
        return;
    }

    const updatedName = document.getElementById('name').value;
    const updatedEmail = document.getElementById('email').value; // Normally not editable

    const updatedData = {
        ...currentUser, // spread existing data (like phone, dob, role)
        name: updatedName,
        email: updatedEmail
    };

    fetch(`/api/users/${currentUser.email}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedData)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.text();
        })
        .then(data => {
            alert(data);
            sessionStorage.setItem("currentUser", JSON.stringify(updatedData));
            loadUserProfile();  // Reload UI
        })
        .catch(error => {
            console.error("Error updating profile:", error);
            alert("Failed to update profile.");
        });
}

function confirmDelete() {
    const currentUser = JSON.parse(sessionStorage.getItem("currentUser"));

    if (!currentUser) {
        alert("No user is currently logged in.");
        return;
    }

    const confirmDelete = confirm("Are you sure you want to delete your account? This action cannot be undone.");
    if (confirmDelete) {
        fetch(`/api/users/${currentUser.email}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error(text) });
                }
                return response.text();
            })
            .then(data => {
                alert(data);
                sessionStorage.removeItem("currentUser");
                window.location.href = "login.html";
            })
            .catch(error => {
                console.error("Error deleting account:", error);
                alert("Failed to delete account.");
            });
    }
}
