const jwt = localStorage.getItem("token");
if (!jwt){
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
            } else throw Error("failed to fetch");
        })
        .then(json => {
            document.getElementById("username").innerHTML = `Username: <span class="text-muted">${json.username}</span>`;
            document.getElementById("password").innerHTML = `Password: <span class="text-muted">${json.password}</span>`;
            document.getElementById("name").innerHTML = `Name: <span class="text-muted">${json.name}</span>`;
            document.getElementById("nickname").innerHTML = `Nickname: <span class="text-muted">${json.nickname}</span>`;
            document.getElementById("age").innerHTML = `Age: <span class="text-muted">${json.age}</span>`;
            document.getElementById("email").innerHTML = `Email: <span class="text-muted">${json.email}</span>`;
            document.getElementById("phone").innerHTML = `Phone: <span class="text-muted">${json.phone}</span>`;
            document.getElementById("address").innerHTML = `Address: <span class="text-muted">${json.address}</span>`;
            document.getElementById("profileImgPath").innerHTML = `Profile Image: <span class="text-muted">${json.profileImgPath}</span>`;
            document.getElementById("role").innerHTML = `Role: <span class="text-muted">${json.role}</span>`;
            document.getElementById("licenseNumber").innerHTML = `License Number: <span class="text-muted">${json.licenseNumber}</span>`;
            document.getElementById("businessNumber").innerHTML = `Business Number: <span class="text-muted">${json.businessNumber}</span>`;
            document.getElementById("emailVerified").innerHTML = `Email Verified: <span class="text-muted">${json.emailVerified}</span>`;


        })
        .catch(e => {
            alert(e.message);
        });

// Đăng xuất
document.getElementById('logout-link').addEventListener('click', function(event) {
    event.preventDefault();
    localStorage.removeItem('token');
    window.location.href = '/views/login';
});

