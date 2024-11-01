const jwt = localStorage.getItem("token");
if (!jwt) {
    location.href = "/views/login";
}

document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    const restaurantId = urlParams.get("restaurantId");

    if (!restaurantId) {
        console.error("No restaurantId found in URL");
        return;
    }

    const viewMenu = document.getElementById("viewMenu");
    viewMenu.href = `/views/link menu của nhà hàng`;

    fetch(`/restaurants/${restaurantId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${jwt}`
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("restaurant-name").textContent = data.name;
            document.getElementById("restaurant-address").textContent = `Address: ${data.address}`;
            document.getElementById("restaurant-phone").textContent = `Phone: ${data.phone}`;
            document.getElementById("restaurant-opening-hours").textContent = `Opening Hours: ${data.openingHours}`;
            document.getElementById("restaurant-cuisine-type").textContent = `Cuisine: ${data.cuisineType}`;
            document.getElementById("restaurant-rating").textContent = `Rating: ${data.rating}`;
            document.getElementById("restaurant-image").src = data.restImage;
            document.getElementById("restaurant-description").textContent = data.description;
            document.getElementById("restaurant-approval-status").textContent = `Approval Status: ${data.approvalStatus}`;
        })
        .catch(error => {
            console.error("Fetch error:", error);
            alert("An error occurred while fetching restaurant details.");
        });
});
