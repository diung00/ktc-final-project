const jwt = localStorage.getItem("token");
if (!jwt){
    location.href = "/views/login";
}
fetch("/users/driver-request-status", {
    method: "get",
    headers: {
        "authorization": `Bearer ${jwt}`
    },
})
    .then(response => {
        if (response.ok) {
            return response.json();
        } else if (response.status === 403) {
            localStorage.removeItem("token");
            location.href = "/views/login";
        } else throw Error("failed to fetch");
    })
    .then(json => {
        document.getElementById("licenseNumber").innerHTML = `License Number: <span class="text-muted">${json.licenseNumber}</span>`;
        document.getElementById("userRole").innerHTML = `UserRole: <span class="text-muted">${json.userRole}</span>`;
        document.getElementById("status").innerHTML = `Status: <span class="text-muted">${json.status}</span>`;
        document.getElementById("rejectionReason").innerHTML = `Rejection Reason: <span class="text-muted">${json.rejectionReason}</span>`;

    })
    .catch(e => {
        alert(e.message);
    });