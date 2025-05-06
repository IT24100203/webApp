const users = [
    { name: "Alice", email: "alice@example.com" },
    { name: "Bob", email: "bob@example.com" }
];

const tableBody = document.getElementById("userTable");

users.forEach(user => {
    const row = document.createElement("tr");
    row.innerHTML = `
    <td>${user.name}</td>
    <td>${user.email}</td>
    <td class="actions">
      <span title="Edit">✏️</span>
      <span title="Delete">🗑️</span>
      <span title="Reset Password">🔑</span>
    </td>
  `;
    tableBody.appendChild(row);
});
