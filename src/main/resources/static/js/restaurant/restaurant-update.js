// JavaScript function to submit restaurant update
async function submitRestaurantUpdate() {
    const restaurantId = localStorage.getItem("restaurantId");
    const name = document.getElementById("name").value;
    const address = document.getElementById("address").value;
    const phone = document.getElementById("phone").value;
    const openingHours = document.getElementById("openingHours").value;
    const cuisineType = document.getElementById("cuisineType").value;
    const description = document.getElementById("description").value;

    const jwt = localStorage.getItem("token");

    // Create the RestaurantUpdateDto object
    const restaurantUpdateDto = {
        name: name,
        address: address,
        phone: phone,
        openingHours: openingHours,
        cuisineType: cuisineType,
        description: description
    };

    try {
        // Send a PUT request to update the restaurant info
        const response = await fetch(`/restaurants/${restaurantId}/update-info`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify(restaurantUpdateDto)
        });

        if (response.ok) {
            const result = await response.text();
            alert(result); // Display success message
        } else {
            // Handle errors
            const errorData = await response.json();
            alert(`Error: ${errorData.message || "Failed to update restaurant information."}`);
        }
    } catch (error) {
        alert(`Error: ${error.message}`);
    }
}

//Function to preload data into the form fields if editing an existing restaurant
async function loadRestaurantData() {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        alert("You must be logged in to access this page.");
        return;
    }
    try {
        // Add Authorization header with the token
        const response = await fetch(`/restaurants/my-restaurant`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${jwt}`,  // Include token in request headers
                "Content-Type": "application/json"
            }
        });

        // Process response if successful
        if (response.ok) {
            const restaurant = await response.json();
            
            // Populate form fields with restaurant data
            // document.getElementById("restaurantId").value = restaurant.id;
            document.getElementById("name").value = restaurant.name;
            document.getElementById("address").value = restaurant.address;
            document.getElementById("phone").value = restaurant.phone;
            document.getElementById("openingHours").value = restaurant.openingHours;
            document.getElementById("cuisineType").value = restaurant.cuisineType;
            document.getElementById("description").value = restaurant.description;

            // Save restaurantId to localStorage
            localStorage.setItem("restaurantId", restaurant.id);
        } else {
            alert("Failed to load restaurant data. Please make sure you are logged in.");
        }
    } catch (error) {
        console.error("Error loading restaurant data:", error);
    }
}

// Restaurant Image Update Fuction

async function submitImageUpdate() {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        alert("You must be logged in to update the restaurant image.");
        return;
    }

    // Lấy restaurantId từ localStorage
    const restaurantId = localStorage.getItem("restaurantId");
    const imageFile = document.getElementById("image").files[0];

    if (!imageFile) {
        alert("Please select an image to upload.");
        return;
    }

    const formData = new FormData();
    formData.append("image", imageFile);

    try {
        const response = await fetch(`/restaurants/${restaurantId}/update-image`, {
            method: "PUT",
            headers: {
                "Authorization": `Bearer ${jwt}`
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


function initializePage() {
    loadRestaurantData();
}

