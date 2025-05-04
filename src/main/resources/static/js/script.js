document.addEventListener('DOMContentLoaded', () => {
    // Login Form Validation
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', (e) => {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();
            const usernameError = document.getElementById('usernameError');
            const passwordError = document.getElementById('passwordError');
            let isValid = true;

            usernameError.classList.add('hidden');
            passwordError.classList.add('hidden');

            if (!username) {
                usernameError.classList.remove('hidden');
                isValid = false;
            }
            if (!password) {
                passwordError.classList.remove('hidden');
                isValid = false;
            }

            if (!isValid) {
                e.preventDefault();
            }
        });
    }

    // Register Form Validation
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', (e) => {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();
            const confirmPassword = document.getElementById('confirmPassword').value.trim();
            const usernameError = document.getElementById('usernameError');
            const passwordError = document.getElementById('passwordError');
            const confirmPasswordError = document.getElementById('confirmPasswordError');
            let isValid = true;

            usernameError.classList.add('hidden');
            passwordError.classList.add('hidden');
            confirmPasswordError.classList.add('hidden');

            if (!username || username.length < 3) {
                usernameError.classList.remove('hidden');
                isValid = false;
            }
            if (!password || password.length < 6) {
                passwordError.classList.remove('hidden');
                isValid = false;
            }
            if (password !== confirmPassword) {
                confirmPasswordError.classList.remove('hidden');
                isValid = false;
            }

            if (!isValid) {
                e.preventDefault();
            }
        });
    }

    // Booking Form Validation
    const bookingForm = document.getElementById('bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', (e) => {
            const startTime = document.getElementById('startTime').value;
            const endTime = document.getElementById('endTime').value;
            const startTimeError = document.getElementById('startTimeError');
            const endTimeError = document.getElementById('endTimeError');
            let isValid = true;

            startTimeError.classList.add('hidden');
            endTimeError.classList.add('hidden');

            if (!startTime) {
                startTimeError.classList.remove('hidden');
                isValid = false;
            }
            if (!endTime) {
                endTimeError.classList.remove('hidden');
                isValid = false;
            }
            if (startTime && endTime && new Date(startTime) >= new Date(endTime)) {
                endTimeError.classList.remove('hidden');
                endTimeError.textContent = 'End time must be after start time';
                isValid = false;
            }

            if (!isValid) {
                e.preventDefault();
            }
        });
    }

    // Add Auditorium Form Validation
    const addAuditoriumForm = document.getElementById('addAuditoriumForm');
    if (addAuditoriumForm) {
        addAuditoriumForm.addEventListener('submit', (e) => {
            const name = document.getElementById('name').value.trim();
            const capacity = document.getElementById('capacity').value;
            const nameError = document.getElementById('nameError');
            const capacityError = document.getElementById('capacityError');
            let isValid = true;

            nameError.classList.add('hidden');
            capacityError.classList.add('hidden');

            if (!name) {
                nameError.classList.remove('hidden');
                isValid = false;
            }
            if (!capacity || capacity <= 0) {
                capacityError.classList.remove('hidden');
                isValid = false;
            }

            if (!isValid) {
                e.preventDefault();
            }
        });
    }

    // Modal Functions for Approve Booking
    window.openApproveModal = function(bookingId) {
        document.getElementById('bookingId').value = bookingId;
        document.getElementById('approveModal').classList.remove('hidden');
    };

    window.closeApproveModal = function() {
        document.getElementById('approveModal').classList.add('hidden');
        document.getElementById('bookingId').value = '';
    };

    // Modal Functions for Logout
    window.openLogoutModal = function() {
        document.getElementById('logoutModal').classList.remove('hidden');
    };

    window.closeLogoutModal = function() {
        document.getElementById('logoutModal').classList.add('hidden');
    };

    // Perform Logout with CSRF Token
    window.performLogout = function() {
        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        fetch('/logout', {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken
            }
        })
        .then(response => {
            if (response.ok || response.redirected) {
                window.location.href = '/login?logout';
            } else {
                alert('Logout failed. Please try again.');
            }
        })
        .catch(() => {
            alert('Logout failed. Please try again.');
        });
    };
});