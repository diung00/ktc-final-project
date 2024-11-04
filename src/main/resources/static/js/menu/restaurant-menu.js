function fetchRestaurantById(restaurantId) {
    const jwt = localStorage.getItem("token");

    return fetch(`/restaurants/${restaurantId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwt}`
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Unable to fetch restaurant details.');
            }
        });
}

function fetchMenuByRestaurantId(restaurantId) {
    const restaurantDetails = document.getElementById('restaurant-details');
    const menuItemsList = document.getElementById('menu-items');
    const jwt = localStorage.getItem("token");

    console.log("Fetching menu for restaurant ID:", restaurantId);

    fetch(`/menus/restaurants/${restaurantId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwt}`
        }
    })
        .then(response => {
            console.log("Response received:", response);

            if (response.ok) {
                return response.json();
            } else {
                throw new Error('No menu found for this restaurant.');
            }
        })
        .then(menuData => {
            // Fetch restaurant details
            return fetchRestaurantById(restaurantId).then(restaurantData => {
                // Hiển thị thông tin nhà hàng
                restaurantDetails.innerHTML = `<h2>${restaurantData.name} - Menu</h2>`;

                // Hiển thị danh sách các mục thực đơn
                menuItemsList.innerHTML = ''; // Clear existing menu items

                menuData.forEach(menu => {
                    const menuItem = document.createElement('div');
                    menuItem.classList.add('list-group-item', 'mb-3'); // Add some margin for spacing

                    // Tạo nội dung cho mỗi mục thực đơn
                    menuItem.innerHTML = `
                    <h5>${menu.name} - $${menu.price.toFixed(2)}</h5>
                    <p>${menu.description || 'No description available.'}</p>
                    <p><strong>Status:</strong> ${menu.menuStatus}</p>
                    <p><strong>Preparation Time:</strong> ${menu.preparationTime} minutes</p>
                    <p><strong>Cuisine Type:</strong> ${menu.cuisineType}</p>
                    ${menu.imagePath ? `<img src="${menu.imagePath}" alt="${menu.name}" style="width: 100%; height: auto;" />` : `<p>No image available</p>`}
                `;
                    const orderButton = document.createElement('button');
                    orderButton.textContent = 'Order';
                    orderButton.classList.add('btn', 'btn-success', 'mt-2');
                    orderButton.addEventListener('click', () => {
                        createOrder(menu, restaurantId);
                    });
                    menuItem.appendChild(orderButton);

                    // Thêm item vào danh sách
                    menuItemsList.appendChild(menuItem);
                });
            });
        })
        .catch(error => {
            console.error('Fetch error:', error);
            restaurantDetails.textContent = 'Error connecting to the server. Details: ' + error.message;
        });
}

// Lấy restaurantId từ URL
const params = new URLSearchParams(window.location.search);
const restaurantId = params.get('id'); // Lấy giá trị của 'id' từ query string

if (restaurantId) {
    fetchMenuByRestaurantId(restaurantId); // Gọi hàm với restaurantId
} else {
    console.error('No restaurant ID found in the URL.');
}
