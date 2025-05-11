function handleForgotPassword(event) {
    event.preventDefault(); // prevent actual form submission

    const email = document.getElementById("email").value.trim();

    if (!email) {
        alert("Please enter your email.");
        return false;
    }

    alert("Password reset functionality is not implemented on the server-side.");
    return false;
}