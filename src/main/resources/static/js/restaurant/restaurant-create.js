// Function to create a new restaurant
async function createRestaurant() {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        alert("You must be logged in to create a restaurant.");
        return;
    }

    const restaurantData = {
        name: document.getElementById("name").value,
        address: document.getElementById("address").value,
        phone: document.getElementById("phone").value,
        openingHours: document.getElementById("openingHours").value,
        cuisineType: document.getElementById("cuisineType").value,
        description: document.getElementById("description").value
    };

    try {
        // Call API to create a new restaurant
        const response = await fetch(`/restaurants/open`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${jwt}`,  // Include token in request headers
                "Content-Type": "application/json"
            },
            body: JSON.stringify(restaurantData)
        });

        if (response.ok) {
            alert("Restaurant created successfully.");
            // Call to get the restaurant ID
            await loadRestaurantId();
        } else {
            alert("Failed to create restaurant.");
        }
    } catch (error) {
        console.error("Error creating restaurant:", error);
    }
}

// Function to load the restaurant ID after creation
async function loadRestaurantId() {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        alert("You must be logged in to access this page.");
        return;
    }

    try {
        // Fetch the restaurant data for the logged-in user
        const response = await fetch(`/restaurants/my-restaurant`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${jwt}`,  // Include token in request headers
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const restaurant = await response.json();
            const restaurantId = restaurant.id; // get id from response
            localStorage.setItem("restaurantId", restaurantId); // save ID into localStorage
            
            // call uploadImage
            await uploadImage(restaurantId);
            
            // Xóa form
            clearForm();
        } else {
            alert("Failed to load restaurant data. Please make sure you are logged in.");
        }
    } catch (error) {
        console.error("Error loading restaurant ID:", error);
    }
}

// Function to upload an image for the restaurant
async function uploadImage(restaurantId) {
    const imageFile = document.getElementById("image").files[0];
    const restaurantId = localStorage.getItem("restaurantId");

    if (!imageFile) {
        alert("Please select an image to upload.");
        return;
    }

    const formData = new FormData();
    formData.append("image", imageFile);

    const jwt = localStorage.getItem("token");
    if (!jwt) {
        alert("You must be logged in to upload an image.");
        return;
    }

    try {
        const response = await fetch(`/restaurants/${restaurantId}/update-image`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${jwt}` // Include token in request headers
            },
            body: formData
        });

        if (response.ok) {
            alert("Restaurant image updated successfully.");
        } else {
            alert("Failed to update restaurant image.");
        }
    } catch (error) {
        console.error("Error updating restaurant image:", error);
    }
}

// Clear form fields
function clearForm() {
    document.getElementById("name").value = '';
    document.getElementById("address").value = '';
    document.getElementById("phone").value = '';
    document.getElementById("openingHours").value = '';
    document.getElementById("cuisineType").value = '';
    document.getElementById("description").value = '';
    document.getElementById("image").value = ''; // Reset image input
}
