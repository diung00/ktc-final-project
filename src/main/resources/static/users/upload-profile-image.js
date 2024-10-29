const jwt = localStorage.getItem("jwt");
if (!jwt) {
    location.href = "/views/login";
}

document.getElementById("uploadProfileImageForm").addEventListener("submit", function(event) {
    event.preventDefault();

    const formData = new FormData();
    const imageFile = document.getElementById("image").files[0];

    if (imageFile) {
        formData.append("image", imageFile);
    }

    fetch("/users/upload-profile-image", {
        method: "POST",
        headers: {
            "authorization": `Bearer ${jwt}`
        },
        body: formData
    })
        .then(response => {
            if (response.ok) {
                alert("Upload image profile successful!");
                location.href = "/views/get-my-profile";
            } else {
                throw new Error("Failed to upload image");
            }
        })
        .catch(e => {
            console.error(e);
            alert(e.message);
        });
});
