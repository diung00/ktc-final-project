async function fetchCurrentUser() {
    const jwt = localStorage.getItem("token");

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
                    <td>${menu.name}</td>
                    <td>${menu.price.toFixed(2)}</td>
                    <td>${menu.description || 'No description available.'}</td>
                    <td>${menu.menuStatus}</td>
                    <td>${menu.preparationTime}</td>
                    <td>${menu.cuisineType}</td>
                    <td><img src="${menu.imagePath}" alt="${menu.name}" style="width: 90px; height: auto;"></td>
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

            // Nút Order
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

                    // Lưu thông tin đơn hàng vào sessionStorage
                    const orderData = {
                        userId: currentUser.id,
                        deliveryAddress: currentUser.deliveryAddress,
                        restaurantId: restaurantId,
                        orderDate: new Date().toISOString(),
                        orderStatus: 'Pending',
                        totalMenusPrice: orderDetails.reduce((total, item) => {
                            const menuItem = menuData.find(m => m.id === item.menuId);
                            return total + (menuItem.price * item.quantity);
                        }, 0),
                        shippingFee: 3000,
                        totalAmount: orderDetails.reduce((total, item) => {
                            const menuItem = menuData.find(m => m.id === item.menuId);
                            return total + (menuItem.price * item.quantity);
                        }, 0) + 5000, // Tổng cộng
                        estimatedArrivalTime: new Date(Date.now() + 30 * 60 * 1000).toISOString() // Thời gian giao dự kiến
                    };

                    sessionStorage.setItem('orderData', JSON.stringify(orderData));
                    window.location.href = '/views/order';
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

const urlParams = new URLSearchParams(window.location.search);
const restaurantId = urlParams.get('id');
fetchMenuByRestaurantId(restaurantId);
