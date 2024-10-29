const jwt = localStorage.getItem("jwt");
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
    if (response.ok){
        return response.json();
    }
    else if (response.status === 403){
        localStorage.removeItem("jwt");
        location.href = "/views/login";
    }
    else throw Error ("failed to fetch");
})
.then(json => {
    document.getElementById("username").innerText = json.username;
    document.getElementById("password").innerText = json.password;
    document.getElementById("name").innerText = json.name;
    document.getElementById("nickname").innerText = json.nickname;
    document.getElementById("age").innerText = json.age;
    document.getElementById("email").innerText = json.email;
    document.getElementById("phone").innerText = json.phone;
    document.getElementById("address").innerText = json.address;
    document.getElementById("profileImgPath").innerText = json.profileImgPath;
    document.getElementById("role").innerText = json.role;
    document.getElementById("licenseNumber").innerText = json.licenseNumber;
    document.getElementById("businessNumber").innerText = json.businessNumber;
    document.getElementById("emailVerified").innerText = json.emailVerified;

})
    .catch(e => {
        alert(e.message);
    });
