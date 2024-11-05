
async function loadMenuData(menuId) {
    const response = await fetch(`/menus/${menuId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("token")}`
        }
    });

    if (!response.ok) {
        alert('Failed to load menu data.');
        return;
    }

    const menuData = await response.json();
    document.getElementById('name').value = menuData.name;
    document.getElementById('price').value = menuData.price;
    document.getElementById('description').value = menuData.description;
    document.getElementById('menuStatus').value = menuData.menuStatus;
    document.getElementById('preparationTime').value = menuData.preparationTime;
    document.getElementById('cuisineType').value = menuData.cuisineType;
}

async function updateMenu(menuId) {
    const updatedMenu = {
        name: document.getElementById('name').value,
        price: parseFloat(document.getElementById('price').value),
        description: document.getElementById('description').value,
        menuStatus: document.getElementById('menuStatus').value,
        preparationTime: parseInt(document.getElementById('preparationTime').value),
        cuisineType: document.getElementById('cuisineType').value,
    };

    const response = await fetch(`/menus/update/${menuId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem("token")}`
        },
        body: JSON.stringify(updatedMenu)
    });

    if (response.ok) {
        alert('Menu updated successfully.');
        window.location.href = `/views/menu`;
    } else {
        alert('Failed to update menu.');
    }
}

// Thêm sự kiện cho nút cập nhật
document.getElementById('update-menu-btn').addEventListener('click', function () {
    const menuId = new URLSearchParams(window.location.search).get('id'); // Lấy menuId từ URL
    updateMenu(menuId);
});

// Tải dữ liệu menu khi trang được tải
document.addEventListener('DOMContentLoaded', function () {
    const menuId = new URLSearchParams(window.location.search).get('id'); // Lấy menuId từ URL
    loadMenuData(menuId);
});