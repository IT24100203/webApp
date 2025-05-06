function enableEdit(id) {
    const input = document.getElementById(id);
    input.removeAttribute('readonly');
    input.focus();
}

function clearField(id) {
    const input = document.getElementById(id);
    input.value = '';
}

function confirmDelete() {
    const confirmDelete = confirm("Are you sure you want to delete your account? This action cannot be undone.");
    if (confirmDelete) {
        // Simulate deletion action — replace with backend call later
        alert("Your account has been deleted.");
        // Redirect to login
        window.location.href = "login.html";
    }
}
