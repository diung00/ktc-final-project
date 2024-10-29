const jwt = localStorage.getItem("jwt");
if (!jwt){
    location.href = "/views/login";
}

document.getElementById("sendVerificationCode").addEventListener("click", function() {
    const email = document.getElementById("email").value;

    if (!email) {
        alert("Please enter your email.");
        return;
    }

    fetch("/users/request-password-reset", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: email })
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("Failed to send verification code");
            }
        })
        .then(message => {
            alert(message);
        })
        .catch(e => {
            console.error(e);
            alert(e.message);
        });
});

document.getElementById("passwordResetForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const formData = {
        email: document.getElementById("email").value,
        code: document.getElementById("code").value,
        newPassword: document.getElementById("newPassword").value
    };

    fetch("/users/reset-password", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("Failed to change password");
            }
        })
        .then(message => {
            alert(message);
        })
        .catch(error => {
            console.error(error);
            alert(error.message);
        });
});