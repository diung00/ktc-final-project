if (typeof jwt === 'undefined') {
    jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
    }
}

// Lấy ID từ URL
const urlParams = new URLSearchParams(window.location.search);
const restaurantId = urlParams.get("restId");

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
            const restaurantInfoDiv = document.querySelector(".restaurant-info-container");
            restaurantInfoDiv.innerHTML = `
            <h2>${restaurant.name}</h2>
            <p>주소: ${restaurant.address}</p>
            <p>전화: ${restaurant.phone}</p>
            <p>영업 시간: ${restaurant.openingHours}</p>
            <p>평점: ${restaurant.rating}</p>
            <p>설명: ${restaurant.description}</p>`;

            sessionStorage.setItem('restaurantName', restaurant.name)
            console.log(restaurant.name);

            const restaurantImageDiv = document.querySelector(".image-container");
            restaurantImageDiv.innerHTML = `<img src="${restaurant.restImage}" alt="${restaurant.name}" />`;
        })

        .catch(error => console.error("Fetch error:", error));
} else {
    console.error("No restaurant ID provided in the URL.");
}

async function fetchCurrentUser() {
    const response = await fetch('/users/get-my-profile', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}`,
            'Content-Type': 'application/json'
        }
    });

    if (response.ok) {
        const userData = await response.json();
        return userData;
    } else {
        throw new Error('Failed to fetch user data');
    }
}

function fetchMenuByRestaurantId(restaurantId) {
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
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('No menu found for this restaurant.');
            }
        })
        .then(menuData => {
            console.log("Menu Data:", menuData);

            menuItemsList.innerHTML = '';

            menuData.forEach(menu => {
                const menuRow = document.createElement('tr');
                menuRow.innerHTML = `
                    <td><img src="${menu.imagePath}" alt="${menu.name}" style="width: 90px; height: auto;"></td>
                    <td>${menu.name}</td>
                    <td>${menu.description || 'No description available.'}</td>
                    <td>${menu.price.toFixed(0)}</td>
                    <td>
                        <button class="decrease-quantity" data-id="${menu.id}">-</button>
                        <span class="quantity" id="quantity-${menu.id}">0</span>
                        <button class="increase-quantity" data-id="${menu.id}">+</button>
                    </td>
                `;

                menuItemsList.appendChild(menuRow);
            });

            document.querySelectorAll('.increase-quantity').forEach(button => {
                button.addEventListener('click', function() {
                    const menuId = this.getAttribute('data-id');
                    const quantityElement = document.getElementById(`quantity-${menuId}`);
                    quantityElement.textContent = parseInt(quantityElement.textContent) + 1;
                });
            });

            document.querySelectorAll('.decrease-quantity').forEach(button => {
                button.addEventListener('click', function() {
                    const menuId = this.getAttribute('data-id');
                    const quantityElement = document.getElementById(`quantity-${menuId}`);
                    const currentQuantity = parseInt(quantityElement.textContent);
                    if (currentQuantity > 0) {
                        quantityElement.textContent = currentQuantity - 1;
                    }
                });
            });

            // Order Button
            document.getElementById('order-button').addEventListener('click', async () => {
                try {
                    const currentUser = await fetchCurrentUser(); // Lấy thông tin người dùng

                    const orderDetails = [];
                    menuData.forEach(menu => {
                        const quantity = parseInt(document.getElementById(`quantity-${menu.id}`).textContent);
                        if (quantity > 0) {
                            orderDetails.push({
                                menuId: menu.id,
                                quantity: quantity
                            });
                        }
                    });
                    const note = document.getElementById('order-note').value;

                    // Lưu thông tin đơn hàng vào sessionStorage
                    const orderData = {
                        userId: currentUser.id,
                        deliveryAddress: currentUser.address,
                        restaurantId: restaurantId,
                        restaurantName: sessionStorage.getItem("restaurantName"),
                        orderStatus: 'Pending',
                        totalMenusPrice: orderDetails.reduce((total, item) => {
                            const menuItem = menuData.find(m => m.id === item.menuId);
                            return total + (menuItem.price * item.quantity);
                        }, 0),
                        shippingFee: 3000,
                        totalAmount: orderDetails.reduce((total, item) => {
                            const menuItem = menuData.find(m => m.id === item.menuId);

                            return total + (menuItem.price * item.quantity);
                        }, 0) + 3000, // Tổng cộng
                        note: note,
                        orderDetails: orderDetails
                    };
                    console.log("order details", orderDetails)

                    sessionStorage.setItem('orderData', JSON.stringify(orderData));
                    window.location.href = '/views/order-confirm';
                } catch (error) {
                    console.error('Error fetching user data:', error);
                    alert('Failed to fetch user data. Please try again.');
                }
            });
        })
        .catch(error => {
            console.error('Fetch error:', error);
            menuItemsList.innerHTML = `<tr><td colspan="9">Error connecting to the server. Details: ${error.message}</td></tr>`;
        });
}

fetchMenuByRestaurantId(restaurantId);
