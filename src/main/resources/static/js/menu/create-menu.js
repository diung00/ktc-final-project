document.addEventListener("DOMContentLoaded", () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
        return;
    }

    const menuForm = document.getElementById("menu-form");
    menuForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const name = document.getElementById("name").value;
        const price = parseFloat(document.getElementById("price").value, 10);
        const description = document.getElementById("description").value;
        const menuStatus = document.getElementById("menuStatus").value;
        const preparationTime = parseInt(document.getElementById("preparationTime").value, 10);
        const cuisineType = document.getElementById("cuisineType").value;

        // Create menuData object
        const menuData = {
            name: name,
            price: price,
            description: description,
            menuStatus: menuStatus,
            preparationTime: preparationTime,
            cuisineType: cuisineType
        };

        fetch(`/menus/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify(menuData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
              //  const responseMessage = document.getElementById("response-message");
              //  responseMessage.innerHTML = `<p>Menu created successfully: ${data.name}</p>`;
                window.location.href = `/views/menu`;
            })
            .catch(error => {
                console.error("Fetch error:", error);
                const responseMessage = document.getElementById("response-message");
                responseMessage.innerHTML = `<p>Error creating menu: ${error.message}</p>`;
            });
    });
});
