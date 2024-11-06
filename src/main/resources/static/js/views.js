document.addEventListener("DOMContentLoaded", () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
    }

    const searchButton = document.getElementById("searchButton");
    const searchInput = document.getElementById("searchInput");

    const validCuisineTypes = [
        "KOREAN_FOOD",
        "JAPANESE_FOOD",          // (일식)
        "CHINESE_FOOD",           // (중식)
        "WESTERN_FOOD",           // (양식)
        "FAST_FOOD",              // (패스트푸드)
        "CAFE_DESSERT",           // (카페/디저트)
        "CHICKEN",                // 치킨)
        "PIZZA",                  // (피자)
        "ASIAN_FOOD",             // (동남아 음식)
        "INTERNATIONAL_FOOD"      // 기타 세계 음식
    ];

    searchButton.addEventListener("click", () => {
        const searchTerm = searchInput.value.trim(); // Lấy giá trị từ ô input

        if (!searchTerm) {
            alert("Please enter a search term.");
            return;
        }

        const isCuisineSearch = validCuisineTypes.some(cuisine => cuisine.toLowerCase() === searchTerm.toLowerCase());
        const isMenuSearch = !isCuisineSearch;

        let fetchApi;

        if (isMenuSearch) {
            // Fetch API tìm kiếm theo tên món ăn
            fetchApi = fetch(`/restaurants/within-radius/menu?menuName=${searchTerm}`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${jwt}`,
                },
            });
        } else if (isCuisineSearch) {
            // Fetch API tìm kiếm theo loại món ăn
            fetchApi = fetch(`/restaurants/within-radius/cuisine?cuisineType=${searchTerm}`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${jwt}`,
                },
            });
        } else {
            alert("유효한 검색어를 입력해주세요 (메뉴 이름 또는 요리 유형)");
            return;
        }
        fetchApi
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                const restaurantList = document.getElementById("restaurant-list-by-menu");
                restaurantList.innerHTML = "";  // Xóa kết quả cũ

                if (data.length === 0) {
                    restaurantList.innerHTML = "<p>일치한 결과가 없습니다!</p>";
                } else {
                    data.forEach(restaurant => {
                        const restaurantDiv = document.createElement("div");
                        restaurantDiv.classList.add("restaurant");
                        restaurantDiv.innerHTML = `
                            <h2><a href="/views/get-one-restaurant?id=${restaurant.id}">${restaurant.name}</a></h2>
                            <p>Address: ${restaurant.address}</p>
                            <p>Phone: ${restaurant.phone}</p>
                            <p>Opening Hours: ${restaurant.openingHours}</p>
                            <p>Cuisine: ${restaurant.cuisineType}</p>
                            <p>Rating: ${restaurant.rating}</p>
                            <img src="${restaurant.restImage}" alt="${restaurant.name}" />
                            <p>${restaurant.description}</p>
                            <p>Status: ${restaurant.approvalStatus}</p>
                        `;

                        restaurantList.appendChild(restaurantDiv);
                    });
                }
            })
            .catch(error => {
                console.error("Fetch error:", error);
                alert("An error occurred while fetching restaurant data.");
            });
    });
});