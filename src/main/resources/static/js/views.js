const jwt = localStorage.getItem("token");
if (!jwt) {
    location.href = "/views/login";
}

document.getElementById("search-section").addEventListener("submit", function(event) {
    event.preventDefault();

    const searchInput = document.getElementById("searchInput").value;
    console.log(searchInput);

    // Kiểm tra giá trị searchInput trước khi gọi API
    if (!searchInput) {
        alert("Please input something to search");
        return;
    }

    const searchRequest = { searchInput: searchInput };


    fetch("/restaurants/within-radius/menu", {
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
        restaurantList.innerHTML = ""; // Clear previous search results

        if (data.length === 0) {
            alert("No results found for your search.");
        } else {
            data.forEach(restaurant => {
                const restaurantDiv = document.createElement("div");
                restaurantDiv.classList.add("restaurant");
                restaurantDiv.innerHTML = `
                    <h2><a href="/restaurant-details.html?restaurantId=${restaurant.id}">${restaurant.name}</a></h2>
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
