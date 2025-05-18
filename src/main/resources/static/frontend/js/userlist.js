document.addEventListener("DOMContentLoaded", () => {
    loadUsers();

    const searchInput = document.getElementById("searchInput");
    searchInput.addEventListener("input", filterUsers);
});

let allUsers = [];

function loadUsers() {
    fetch("/api/users")
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch users");
            }
            return response.json();
        })
        .then(users => {
            allUsers = users; // Store the fetched users
            renderUserTable(users); // Initial rendering
        })
        .catch(error => {
            console.error("Error loading user data:", error);
            console.error("Error details:", error.message, error.stack);
            alert("Failed to load users.");
        });
}

function renderUserTable(users) {
    const tableBody = document.getElementById("userTable");
    tableBody.innerHTML = ""; // Clear existing rows
    users.forEach((user, index) => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${index + 1}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${user.role}</td>
            <td>${user.phone}</td>
            <td>${user.dob}</td>
            <td class="actions">
                <span title="Edit" onclick="openEditModal('${user.email}')">✏️</span>
                <span title="Delete" onclick="deleteUser('${user.email}')">🗑️</span>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function filterUsers(event) {
    const searchTerm = event.target.value.toLowerCase();
    const filteredUsers = allUsers.filter(user => {
        return (
            user.name.toLowerCase().includes(searchTerm) ||
            user.email.toLowerCase().includes(searchTerm) ||
            user.phone.toLowerCase().includes(searchTerm) ||
            user.dob.toLowerCase().includes(searchTerm) ||
            user.role.toLowerCase().includes(searchTerm)
        );
    });
    renderUserTable(filteredUsers);
}

function openEditModal(email) {
    const userToEdit = allUsers.find(user => user.email === email);
    if (!userToEdit) {
        alert("User not found!");
        return;
    }

    const modal = document.getElementById("editUserModal");
    modal.style.display = "block";

    document.getElementById("editUserEmail").value = userToEdit.email;
    document.getElementById("editUserName").value = userToEdit.name;
    document.getElementById("editUserPassword").value = userToEdit.password;
    document.getElementById("editUserPhone").value = userToEdit.phone || "";
    document.getElementById("editUserDob").value = userToEdit.dob || "";
    document.getElementById("editUserRole").value = userToEdit.role;
}

function closeEditModal() {
    const modal = document.getElementById("editUserModal");
    modal.style.display = "none";
}

function saveEditedUser() {
    const email = document.getElementById("editUserEmail").value;
    const name = document.getElementById("editUserName").value;
    const password = document.getElementById("editUserPassword").value;
    const phone = document.getElementById("editUserPhone").value;
    const dob = document.getElementById("editUserDob").value;
    const role = document.getElementById("editUserRole").value;

    const updatedUser = { name, email, password, phone, dob, role };

    fetch(`/api/users/${email}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedUser),
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.text();
        })
        .then(data => {
            alert(data);
            loadUsers();
            closeEditModal();
        })
        .catch(error => {
            console.error("Error updating user:", error);
            alert("Failed to update user.");
        });
}

function deleteUser(email) {
    if (confirm(`Are you sure you want to delete the user with email: ${email}?`)) {
        fetch(`/api/users/${email}`, {
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
                loadUsers();
            })
            .catch(error => {
                console.error("Error deleting user:", error);
                alert("Failed to delete the user.");
            });
    }
}