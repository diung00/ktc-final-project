const jwt = localStorage.getItem("token");
if (!jwt) {
    location.href = "/views/login";
}

document.getElementById("ownerRequestForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const businessNumber = document.getElementById("businessNumber").value;

    fetch(`/users/request-owner-role?businessNumber=${encodeURIComponent(businessNumber)}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${jwt}`
        }
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error("Failed to send role request");
            }
        })
        .then(message => alert(message))
        .catch(e => {
            console.error(e);
            alert(e.message);
        });
});
