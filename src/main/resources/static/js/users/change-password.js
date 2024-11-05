const jwt = localStorage.getItem("token");
if (!jwt) {
    location.href = "/views/login";
}

// Cập nhật mật khẩu
document.getElementById("changePasswordForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const currentPassword = document.getElementById("currentPassword").value;
    const newPassword = document.getElementById("newPassword").value;

    const passwordChangeRequest = {
        currentPassword: currentPassword,
        newPassword: newPassword
    };

    fetch("/users/change-password", {
        method: "post",
        headers: {
            "Content-Type": "application/json",
            "authorization": `Bearer ${jwt}`
        },
        body: JSON.stringify(passwordChangeRequest)
    })
        .then(response => {
            if (response.ok) {
                alert("Password changed successfully!");
                location.href = "/views/get-my-profile";
            } else if (response.status === 403) {
                alert("Current password is incorrect.");
            } else {
                throw new Error("Failed to change password");
            }
        })
        .catch(e => {
            console.error(e);
            alert(e.message);
        });
});


