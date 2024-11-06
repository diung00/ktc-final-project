// Wait for the DOM to fully load before running any script
document.addEventListener("DOMContentLoaded", function() {

  // Reference to the elements
  const sendVerificationCodeButton = document.getElementById('sendVerificationCode');
  const passwordResetForm = document.getElementById('passwordResetForm');
  const emailInput = document.getElementById('email');
  const codeInput = document.getElementById('code');
  const newPasswordInput = document.getElementById('newPassword');

  // Handle sending the verification code
  sendVerificationCodeButton.addEventListener('click', function() {
    const email = emailInput.value;

    if (!email) {
      alert('Please enter your email address.');
      return;
    }

    // Send an AJAX request to send a verification code to the email
    fetch('/send-verification-code', { // You will need to replace this endpoint with your actual one
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email: email })
    })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        alert('Verification code sent to your email!');
      } else {
        alert('Failed to send verification code. Please try again.');
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Something went wrong. Please try again.');
    });
  });

  // Handle form submission to reset the password
  passwordResetForm.addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form submission from reloading the page

    const email = emailInput.value;
    const code = codeInput.value;
    const newPassword = newPasswordInput.value;

    // Validate the inputs
    if (!email || !code || !newPassword) {
      alert('Please fill in all fields.');
      return;
    }

    // Send an AJAX request to reset the password
    fetch('/reset-password', { // Replace this with your actual reset endpoint
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, code, newPassword })
    })
    .then(response => response.json())
    .then(data => {
      if (data.success) {
        alert('Password changed successfully!');
        window.location.href = '/login'; // Redirect to login page after successful password change
      } else {
        alert('Failed to change password. Please check your verification code and try again.');
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Something went wrong. Please try again.');
    });
  });
});
