document.addEventListener("DOMContentLoaded", () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
        return;
    }

    // Lấy menuId từ URL
    const urlParams = new URLSearchParams(window.location.search);
    const menuId = urlParams.get("id");

    const deleteButton = document.getElementById("delete-menu-btn");
    deleteButton.addEventListener("click", (event) => {
        event.preventDefault();

        if (!confirm("Are you sure you want to delete this menu item?")) return;

        fetch(`/menus/delete/${menuId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            }
        })
        .then(response => {
            if (!response.ok) throw new Error("Failed to delete menu.");
            alert("Menu item deleted successfully.");
            location.href = '/views/restaurant-menu';
        })
        .catch(error => {
            console.error("Error deleting menu:", error);
            alert("Error deleting menu: " + error.message);
        });
    });
});
