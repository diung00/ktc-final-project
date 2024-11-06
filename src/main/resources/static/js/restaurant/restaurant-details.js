const jwt = localStorage.getItem("token");
if (!jwt) {
    location.href = "/views/login";
}

// Lấy ID từ URL
const urlParams = new URLSearchParams(window.location.search);
const restaurantId = urlParams.get("id");

if (restaurantId) {
    fetch(`/restaurants/${restaurantId}`, {
        method: "GET",
        headers: {
            "authorization": `Bearer ${jwt}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(restaurant => {
            const restaurantInfoDiv = document.getElementById("restaurant-info");
            restaurantInfoDiv.innerHTML = `
            <h2>${restaurant.name}</h2>
            <p>Address: ${restaurant.address}</p>
            <p>Phone: ${restaurant.phone}</p>
            <p>Opening Hours: ${restaurant.openingHours}</p>
            <p>Cuisine: ${restaurant.cuisineType}</p>
            <p>Rating: ${restaurant.rating}</p>
            <img src="${restaurant.restImage}" alt="${restaurant.name}" />
            <p>${restaurant.description}</p>
            <p>Status: ${restaurant.approvalStatus}</p>
        `;

            // Tạo nút "View Menu"
            const viewMenuButton = document.createElement('button');
            viewMenuButton.textContent = '메뉴 보기';
            viewMenuButton.classList.add('btn', 'btn-primary', 'mt-3'); // Thêm các lớp CSS để định kiểu

            // Thêm sự kiện cho nút "View Menu"
            viewMenuButton.addEventListener('click', () => {
                window.location.href = `/views/view-menu?id=${restaurantId}`;
            });

            // Thêm nút vào restaurantInfoDiv
            restaurantInfoDiv.appendChild(viewMenuButton);
        })

        .catch(error => console.error("Fetch error:", error));
} else {
    console.error("No restaurant ID provided in the URL.");
}
