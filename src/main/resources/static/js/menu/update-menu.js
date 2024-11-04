document.addEventListener("DOMContentLoaded", async () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
        return;
    }

    // Lấy menuId từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const menuId = urlParams.get("id");

    // Hàm tải thông tin menu hiện tại để hiển thị trên form
    async function loadMenuData() {
        try {
            const response = await fetch(`/menus/${menuId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${jwt}`
                }
            });

            if (!response.ok) throw new Error("Failed to fetch menu data.");
            const menuData = await response.json();

            document.getElementById("name").value = menuData.name;
            document.getElementById("price").value = menuData.price;
            document.getElementById("description").value = menuData.description || '';
            document.getElementById("menuStatus").value = menuData.menuStatus;
            document.getElementById("preparationTime").value = menuData.preparationTime;
            document.getElementById("cuisineType").value = menuData.cuisineType;
        } catch (error) {
            console.error("Error loading menu data:", error);
            alert("Error loading menu data: " + error.message);
        }
    }

    // Tải dữ liệu menu khi trang được tải
    loadMenuData();

    const menuForm = document.getElementById("menu-form");
    menuForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const name = document.getElementById("name").value;
        const price = parseInt(document.getElementById("price").value, 10);
        const description = document.getElementById("description").value;
        const menuStatus = document.getElementById("menuStatus").value;
        const preparationTime = parseInt(document.getElementById("preparationTime").value, 10);
        const cuisineType = document.getElementById("cuisineType").value;

        // Đối tượng dữ liệu cập nhật menu
        const menuData = {
            name: name,
            price: price,
            description: description,
            menuStatus: menuStatus,
            preparationTime: preparationTime,
            cuisineType: cuisineType
        };

        // Gửi yêu cầu cập nhật
        fetch(`/menus/update/${menuId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify(menuData)
        })
        .then(response => {
            if (!response.ok) throw new Error("Failed to update menu.");
            return response.json();
        })
        .then(data => {
            const responseMessage = document.getElementById("response-message");
            responseMessage.innerHTML = `<p>Menu updated successfully: ${data.name}</p>`;
            setTimeout(() => location.href = '/views/restaurant-menu', 2000);
        })
        .catch(error => {
            console.error("Error updating menu:", error);
            const responseMessage = document.getElementById("response-message");
            responseMessage.innerHTML = `<p>Error updating menu: ${error.message}</p>`;
        });
    });
});
