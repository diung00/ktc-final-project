// Function to preload data into the form fields if editing an existing restaurant
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

        if (!response.ok) {
            throw new Error('Failed to fetch restaurant data.');
        }

        const restaurantData = await response.json();

        // Giả sử restaurantData có thuộc tính id
        const restaurantId = restaurantData.id; // Get restaurantId
        localStorage.setItem('restaurantId', restaurantId); // Lưu restaurantId vào localStorage

        fetchMenuByRestaurantId(restaurantId);

        // "메뉴 등록" Button
// Tạo nút bấm dẫn đến trang tạo menu
        const createMenuButton = document.createElement('button');
        createMenuButton.textContent = '메뉴 등록';
        createMenuButton.classList.add('create-menu');
        createMenuButton.addEventListener('click', () => {
            window.location.href = `/views/restaurant-add-menu?id=${restaurantId}`;
        });

// Thêm nút bấm vào createMenuButton
        const buttonContainer = document.getElementById('create-button-container');
        buttonContainer.appendChild(createMenuButton);
    } catch (error) {
        console.error('Error loading restaurant data:', error);
        alert('Error loading restaurant data: ' + error.message);
    }
}

// Function to fetch the restaurant's menu by restaurant ID
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
        menuItemsList.innerHTML = '';

        menuData.forEach(menu => {
            const menuRow = document.createElement('tr');
            menuRow.innerHTML = `
                <td>${menu.id}</td>
                <td>${menu.name}</td>
                <td>${menu.price.toFixed(2)}</td>
                <td>${menu.description || 'No description available.'}</td>
                <td>${menu.menuStatus}</td>
                <td>${menu.preparationTime}</td>
                <td>${menu.cuisineType}</td>
                <td><img src="${menu.imagePath}" alt="${menu.name}" style="width: 90px ; height: auto;"></td>
                <td>
                    <button class="update-menu-btn" data-id="${menu.id}">메뉴 수정</button>
                    <button class="delete-menu-btn" data-id="${menu.id}">메뉴 삭제</button>
                </td> 
            `;

            menuItemsList.appendChild(menuRow);
        });
        //Menu update button
        document.querySelectorAll('.update-menu-btn').forEach(button => {
            button.addEventListener('click', function() {
                const menuId = this.getAttribute('data-id');
                location.href = `/views/restaurant-update-menu?id=${menuId}`;
            });
        });
        //Menu delete button
        // Menu delete button
        document.querySelectorAll('.delete-menu-btn').forEach(button => {
            button.addEventListener('click', function() {
                const menuId = this.getAttribute('data-id');
                if (confirm("Are you sure you want to delete this menu item?")) {
                    fetch(`/menus/delete/${menuId}`, {
                        method: 'DELETE',
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem("token")}`,
                            'Content-Type': 'application/json'
                        }
                    })
                        .then(response => {
                            if (response.ok) {
                                alert('Menu item deleted successfully.');
                                button.closest('tr').remove();
                            } else {
                                return response.json().then(err => {
                                    alert(`Failed to delete menu item: ${err.message || 'Unknown error'}`);
                                });
                            }
                        })
                        .catch(error => {
                            console.error('Error:', error);
                            alert('An error occurred while deleting the menu item: ' + error.message);
                        });
                }
            });
        });

    })
    .catch(error => {
        console.error('Fetch error:', error);
        menuItemsList.innerHTML = `<tr><td colspan="7">Error connecting to the server. Details: ${error.message}</td></tr>`;
    });
}

// Load the restaurant data when the page is loaded
document.addEventListener('DOMContentLoaded', loadRestaurantData);

