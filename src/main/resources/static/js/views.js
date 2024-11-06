if (typeof jwt === 'undefined') {
    const jwt = localStorage.getItem("token");
    if (!jwt) location.href = "/views/login";
}

// Function to search restaurants by menu name
document.getElementById("searchButton").addEventListener("click", function() {
    // Get the menu name from the input field
    const menuName = document.getElementById("searchInput").value; 
    if (menuName) {
        searchRestaurantsByMenuName(menuName);  // Call the function to search restaurants by menu name  
    }
});

function searchRestaurantsByMenuName(menuName) {
    fetch(`/restaurants/within-radius/menu?menuName=${menuName}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}` // Use the jwt token for authentication
        }
    })
    .then(response => response.json()) // Parse the JSON response
    .then(data => {
        displayRestaurants(data); // Display the restaurants in the UI
    })
    .catch(error => {
        console.error('Error:', error); // Handle any errors that occur
    });
}

// Function to find nearby restaurants
document.getElementById("findNearbyRestaurantsButton").addEventListener("click", function() {
    findNearbyRestaurants();  // Call the function to find nearby restaurants
});
function findNearbyRestaurants() {
    fetch(`/restaurants/within-radius`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}` // Use the jwt token for authentication
        }
    })
    .then(response => response.json()) // Parse the JSON response
    .then(data => {
        displayRestaurants(data); // Display the restaurants in the UI
    })
    .catch(error => {
        console.error('Error:', error); // Handle any errors that occur
    });
}

// Function to search restaurants by cuisine type
document.querySelectorAll(".category-item").forEach(item => {
    item.addEventListener("click", function() {
        // Get the cuisine type from the data attribute
        const cuisineType = item.getAttribute("data-cuisine"); 
        searchRestaurantsByCuisine(cuisineType); // Call the function to search restaurants by cuisine type
    });
});
function searchRestaurantsByCuisine(cuisineType) {
    fetch(`/restaurants/within-radius/cuisine?cuisineType=${cuisineType}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}` // Use the jwt token for authentication
        }
    })
    .then(response => response.json()) // Parse the JSON response
    .then(data => {
        displayRestaurants(data); // Display the restaurants in the UI
    })
    .catch(error => {
        console.error('Error:', error); // Handle any errors that occur
    });
}

// Function to display the list of restaurants
function displayRestaurants(restaurants) {
    const restaurantListContainer = document.querySelector("#restaurant-list-by-menu .restaurant-list"); 
    restaurantListContainer.innerHTML = ''; // Clear the existing restaurant list

    if (!Array.isArray(restaurants) || restaurants.length === 0) {
        restaurantListContainer.innerHTML = '<p>No restaurants found</p>'; // Show message if no restaurants found or invalid data
        return;
    }

    // Loop through each restaurant and create the HTML elements to display them
    restaurants.forEach(restaurant => {
        const restaurantItem = document.createElement('div'); 
        restaurantItem.classList.add('restaurant-item'); 

        // Create an image element for the restaurant
        const restaurantImg = document.createElement('div');
        restaurantImg.classList.add('restaurant-img');
        const img = document.createElement('img');
        img.src = restaurant.imageUrl;  // Set the image source
        img.alt = restaurant.name; // Set the alt text for the image
        img.style.width = '100px';
        img.style.height = '100px';
        img.style.objectFit = 'cover'; // Ensure the image fits within the container
        restaurantImg.appendChild(img); // Append the image to the restaurant image div

        // Create a container for the restaurant's information
        const restaurantInfo = document.createElement('div');
        restaurantInfo.classList.add('restaurant-info');
        const restaurantName = document.createElement('a');
        restaurantName.href = `/order/${restaurant.id}`; // Link to the restaurant's order page
        restaurantName.classList.add('restaurant-name');
        restaurantName.textContent = restaurant.name; // Set the restaurant's name
        restaurantInfo.appendChild(restaurantName); // Append the name to the restaurant info div
    
        restaurantItem.appendChild(restaurantImg); // Append the image div to the restaurant item
        restaurantItem.appendChild(restaurantInfo); // Append the info div to the restaurant item
        restaurantListContainer.appendChild(restaurantItem); // Append the restaurant item to the list container
    });
}
