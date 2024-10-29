const container = document.getElementById('container');
const sendCodeButton = document.getElementById('sendCodeButton');
const resetPasswordButton = document.getElementById('resetPasswordButton');
const verificationCodeInput = document.getElementById('verificationCodeInput');
const newPasswordInput = document.getElementById('newPasswordInput');
const emailInput = document.getElementById('emailInput');
const passwordResetForm = document.getElementById('passwordResetForm');
const overlayContainer = document.getElementById('overlayContainer');

// Show overlay while processing requests
function showOverlay() {
    overlayContainer.style.display = 'flex';
}

// Hide overlay after processing is complete
function hideOverlay() {
    overlayContainer.style.display = 'none';
}

// Step 1: Send verification code
sendCodeButton.addEventListener('click', function() {
    const email = emailInput.value.trim();

    if (email) {
        showOverlay(); // Show overlay

        fetch(`/users/request-password-reset?email=${encodeURIComponent(email)}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
        .then(function(response) {
            hideOverlay(); // Hide overlay after receiving response

            if (response.ok) {
                alert(`Verification code sent to: ${email}`);
                passwordResetForm.style.display = "block"; // Show password reset form
            } else {
                return response.json().then(function(data) {
                    alert(data.message);
                });
            }
        })
        .catch(function(error) {
            hideOverlay(); // Hide overlay if an error occurs
            alert('An error occurred. Please try again.');
            console.error(error);
        });
    } else {
        alert('Please enter a valid email address.');
    }
});

// Step 2: Reset password
resetPasswordButton.addEventListener('click', function() {
    const code = verificationCodeInput.value.trim();
    const newPassword = newPasswordInput.value.trim();

    if (code && newPassword) {
        showOverlay(); // Show overlay while processing request

        const requestDto = {
            email: emailInput.value.trim(),
            code: code,
            newPassword: newPassword
        };

        fetch('/users/reset-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestDto)
        })
        .then(function(response) {
            hideOverlay(); // Hide overlay after receiving response

            if (response.ok) {
                alert('Password has been reset successfully.');
                window.location.href = '/login'; // Redirect to login page
            } else {
                return response.json().then(function(data) {
                    alert(data.message);
                });
            }
        })
        .catch(function(error) {
            hideOverlay(); // Hide overlay if an error occurs
            alert('An error occurred. Please try again.');
            console.error(error);
        });
    } else {
        alert('Please enter the verification code and new password.');
    }
});