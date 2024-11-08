// Function to handle profile image upload
async function submitImageUpload() {
    const jwt = localStorage.getItem("token");

    if (!jwt) {
        alert("You must be logged in to upload your profile image.");
        return;
    }

    const imageFile = document.getElementById("image").files[0];

    if (!imageFile) {
        alert("Please select an image to upload.");
        return;
    }

    const formData = new FormData();
    formData.append("image", imageFile);

    try {
        // Send the image to the backend
        const response = await fetch("/users/upload-profile-image", {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${jwt}`  // Add token to headers
            },
            body: formData
        });

        console.log("Response status:", response.status);
        if (response.ok) {
            const result = await response.text();
            alert(result);
        } else {
            const errorData = await response.json();
            console.log("Error response:", errorData);  // Xem chi tiết lỗi trả về từ backend
            alert(`Error: ${errorData.message || "Failed to upload image."}`);
        }
    } catch (error) {
        alert(`Error: ${error.message}`);
    }
}

// Function to display the image preview before upload
document.getElementById("image").addEventListener("change", function(event) {
    var file = event.target.files[0];
    var reader = new FileReader();

    reader.onloadend = function() {
        var previewImg = document.getElementById("previewImg");
        previewImg.src = reader.result;
        previewImg.style.display = "block"; // Show the image preview
    };

    if (file) {
        reader.readAsDataURL(file);
    }
});
