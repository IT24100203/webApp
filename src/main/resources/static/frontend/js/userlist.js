document.addEventListener("DOMContentLoaded", () => {
    loadUsers();
});

function loadUsers() {
    fetch('/api/users')
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch users");
            }
            return response.json();
        })
        .then(users => {
            const tableBody = document.getElementById("userTable");
            tableBody.innerHTML = ""; // Clear existing rows
            users.forEach((user, index) => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${index + 1}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td class="actions">
                        <span title="Edit" onclick="editUser('${user.email}')">✏️</span>
                        <span title="Delete" onclick="deleteUser('${user.email}')">🗑️</span>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        })
        .catch(error => {
            console.error("Error loading user data:", error);
            alert("Failed to load users.");
        });
}


function editUser(email) {
    alert("Edit user functionality not implemented.");
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
