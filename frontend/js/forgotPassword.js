function handleForgotPassword(event) {
    event.preventDefault(); // prevent actual form submission

    const email = document.getElementById("email").value.trim();

    if (!email) {
        alert("Please enter your email.");
        return false;
    }

    alert("If this email exists in our system, a password reset link has been sent.");
    // Here, you would typically send a request to your server or backend.

    return false;
}
