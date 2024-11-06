document.addEventListener('DOMContentLoaded', () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
        return;
    }
});

document.getElementById('updateProfileForm').addEventListener('submit', async function (event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const nickname = document.getElementById('nickname').value;
    const age = document.getElementById('age').value;
//    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const address = document.getElementById('address').value;

    const payload = {
        name,
        nickname,
        age,
//        email,
        phone,
        address
    };

    try {
        const response = await fetch('/users/update-profile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const result = await response.json();
            alert("Profile updated successfully!");
            location.href = "/views/get-my-profile";
            console.log("Updated profile data:", result);
        } else {
            const errorData = await response.json();
            alert("Failed to update profile: " + (errorData.message || errorData.error || "Unknown error"));
            console.error("Error details:", errorData);
        }
    } catch (error) {
        console.error("Unexpected error:", error);
        alert("An unexpected error occurred. Please try again later.");
    }
});
