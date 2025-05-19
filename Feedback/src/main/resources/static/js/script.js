// Custom JavaScript for the Feedback System

document.addEventListener('DOMContentLoaded', function() {
    // Auto-dismiss alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            alert.classList.add('fade');
            setTimeout(function() {
                alert.remove();
            }, 500);
        }, 5000);
    });
    
    // Star rating visual enhancement for add/edit review forms
    const ratingInputs = document.querySelectorAll('input[name="rating"]');
    if (ratingInputs.length > 0) {
        ratingInputs.forEach(function(input) {
            input.addEventListener('change', function() {
                const rating = parseInt(this.value);
                highlightStars(rating);
            });
        });
        
        // Initialize stars based on selected rating
        const selectedRating = document.querySelector('input[name="rating"]:checked');
        if (selectedRating) {
            highlightStars(parseInt(selectedRating.value));
        }
    }
    
    function highlightStars(rating) {
        for (let i = 1; i <= 5; i++) {
            const label = document.querySelector(`label[for="rating${i}"]`);
            if (label) {
                if (i <= rating) {
                    label.innerHTML = `<i class="fas fa-star text-warning"></i>`;
                } else {
                    label.innerHTML = `<i class="far fa-star text-warning"></i>`;
                }
            }
        }
    }
});
