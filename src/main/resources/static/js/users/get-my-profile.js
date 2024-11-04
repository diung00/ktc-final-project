document.addEventListener('DOMContentLoaded', () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
    }

    fetch("/users/get-my-profile", {
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
        } else {
            throw Error("failed to fetch");
        }
    })
    .then(json => {
        console.log(json);
        // Profile img
        document.getElementById("profileImg").src = json.profileImgPath ? json.profileImgPath : '/static/img/replace/user.png';
        
        // General profile information
        document.getElementById("name").innerHTML = `Name: <span class="text-muted">${json.name}</span>`;
        document.getElementById("nickname").innerHTML = `Nickname: <span class="text-muted">${json.nickname}</span>`;
        document.getElementById("age").innerHTML = `Age: <span class="text-muted">${json.age}</span>`;
        document.getElementById("email").innerHTML = `Email: <span class="text-muted">${json.email}</span>`;
        document.getElementById("phone").innerHTML = `Phone: <span class="text-muted">${json.phone}</span>`;
        document.getElementById("address").innerHTML = `Address: <span class="text-muted">${json.address}</span>`;
        document.getElementById("emailVerified").innerHTML = `Email Verified: <span class="text-muted">${json.emailVerified}</span>`;

        // Conditional display based on role
        if (json.role === 'ROLE_DRIVER') {
            if (json.licenseNumber) {
                document.getElementById("licenseNumber").innerHTML = `License Number: <span class="text-muted">${json.licenseNumber}</span>`;
                document.getElementById("licenseNumber").style.display = 'block';
            } else {
                document.getElementById("licenseNumber").style.display = 'none';
            }
            document.getElementById("businessNumber").style.display = 'none';
        } else if (json.role === 'ROLE_OWNER') {
            if (json.businessNumber) {
                document.getElementById("businessNumber").innerHTML = `Business Number: <span class="text-muted">${json.businessNumber}</span>`;
                document.getElementById("businessNumber").style.display = 'block';
            } else {
                document.getElementById("businessNumber").style.display = 'none'; 
            }
            document.getElementById("licenseNumber").style.display = 'none';
        } else {
            document.getElementById("licenseNumber").style.display = 'none'; 
            document.getElementById("businessNumber").style.display = 'none';
        }
    })
    .catch(e => {
        alert(e.message);
    });
})