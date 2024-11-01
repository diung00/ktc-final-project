const jwt = localStorage.getItem("token");
if (!jwt) {
    location.href = "/views/login";
}
//lấy thông tin user
fetch("/users/get-my-profile", {
    method: "get",
    headers: {
        "authorization": `Bearer ${jwt}`
    },
})
    .then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error("Failed to fetch");
        }
    })
    .then(json => {
        document.getElementById("username").value = json.username;
        document.getElementById("name").value = json.name;
        document.getElementById("nickname").value = json.nickname;
        document.getElementById("age").value = json.age;
        document.getElementById("email").value = json.email;
        document.getElementById("phone").value = json.phone;
        document.getElementById("address").value = json.address;
     //   document.getElementById("profileImgPath").value = json.profileImgPath;
     //   document.getElementById("role").innerText = json.role;
        // document.getElementById("licenseNumber").innerText = json.licenseNumber;
     //   document.getElementById("businessNumber").innerText = json.businessNumber;

    })
    .catch(e => {
        console.error(e);
    });

// Cập nhật thông tin user
document.getElementById("updateForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const updatedUser = {
        username: document.getElementById("username").value,
        name: document.getElementById("name").value,
        nickname: document.getElementById("nickname").value,
        age: document.getElementById("age").value,
        email: document.getElementById("email").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value,
      //  profileImgPath: document.getElementById("profileImgPath").value,
       // licenseNumber: document.getElementById("licenseNumber").value,
     //   businessNumber: document.getElementById("businessNumber").value,
    };

    fetch("/users/update-profile", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "authorization": `Bearer ${jwt}`
        },
        body: JSON.stringify(updatedUser)
    })
        .then(response => {
            if (response.ok) {
                alert("Profile updated successfully!");
                location.href = "/views/get-my-profile";
            } else {
                throw new Error("Failed to update profile");
            }
        })
        .catch(e => {
            console.error(e);
            alert(e.message);
        });
});
