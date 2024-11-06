document.addEventListener("DOMContentLoaded", async () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
        return;
    }
    const urlParams = new URLSearchParams(window.location.search);
    const menuId = urlParams.get('menuId');

    // Function to load menu data and populate form
    async function loadMenuData() {
        try {
            console.log("Loading menu with ID:", menuId);

            // Fetch menu data by menuId
            const response = await fetch(`/menus/${menuId}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${jwt}`
                }
            });

            if (!response.ok) throw new Error("Failed to fetch menu data.");
            const menuData = await response.json();

            // Populate form fields with menu data
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

    // Load menu data when the page loads
    loadMenuData();

    // Handle form submission for menu update
    async function submitMenuUpdate() {
        // Get values from form inputs
        const name = document.getElementById("name").value;
        const price = parseInt(document.getElementById("price").value, 10);
        const description = document.getElementById("description").value;
        const menuStatus = document.getElementById("menuStatus").value;
        const preparationTime = parseInt(document.getElementById("preparationTime").value, 10);
        const cuisineType = document.getElementById("cuisineType").value;

        // Create the menu update data object
        const menuUpdateDto = {
            name: name,
            price: price,
            description: description,
            menuStatus: menuStatus,
            preparationTime: preparationTime,
            cuisineType: cuisineType
        };
        try {
            // Send PUT request to update menu
            const response = await fetch(`/menus/update/${menuId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwt}`
                },
                body: JSON.stringify(menuUpdateDto)
            });
            if (response.ok) {
                const result = await response.text();
                alert("Menu updated successfully.");
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.message || "Failed to update menu information."}`);
            }
        } catch (error) {
            alert(`Error: ${error.message}`);
        }
    }

    async function submitImageUpdate() {
        const imageFile = document.getElementById("image").files[0];
    
        if (!imageFile) {
            alert("Please select an image to upload.");
            return;
        }
    
        const formData = new FormData();
        formData.append("image", imageFile); 
    
        try {
            const response = await fetch(`/menus/${menuId}/image`, {
                method: "PUT",
                headers: {
                    'Authorization': `Bearer ${jwt}` 
                },
                body: formData
            });
    
            if (response.ok) {
                const updatedMenu = await response.json();
                alert("Image updated successfully!");
            } else {
                const errorData = await response.json();
                alert("Failed to update image: " + errorData.message);
            }
        } catch (error) {
            console.error("Error updating image:", error);
            alert("Error updating image.");
        }
    }
    // Attach event listeners to specific buttons
    document.getElementById("updateMenuButton").addEventListener("click", submitMenuUpdate);
    document.getElementById("updateImageButton").addEventListener("click", submitImageUpdate);
    
});
