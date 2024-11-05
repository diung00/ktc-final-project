const jwt = localStorage.getItem("token");
if (!jwt) {
    location.href = "/views/login";
}

document.addEventListener("DOMContentLoaded", () => {
    fetch("/restaurants/within-radius", {
        method: "get",
        headers: {
            "authorization": `Bearer ${jwt}`
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            const restaurantList = document.getElementById("restaurant-list");

            if (data.length === 0) {
                restaurantList.innerHTML = "<p>No restaurants found in your area.</p>";
            } else {
                data.forEach(restaurant => {
                    const restaurantDiv = document.createElement("div");
                    restaurantDiv.classList.add("restaurant");
                    restaurantDiv.innerHTML = `
                        <h2><a href="/views/get-one-restaurant?id=${restaurant.id}">${restaurant.name}</a></h2>
                        <p>Address: ${restaurant.address}</p>
                        <p>Phone: ${restaurant.phone}</p>
                        <p>Opening Hours: ${restaurant.openingHours}</p>
                        <p>Cuisine: ${restaurant.cuisineType}</p>
                        <p>Rating: ${restaurant.rating}</p>
                        <img src="${restaurant.restImage}" alt="${restaurant.name}" />
                        <p>${restaurant.description}</p>
                        <p>Status: ${restaurant.approvalStatus}</p>
                    `;

                    restaurantList.appendChild(restaurantDiv);
                });
            }
        })
        .catch(error => console.error("Fetch error:", error));
});
