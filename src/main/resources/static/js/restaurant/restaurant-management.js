console.log("js is loading")

function fetchMyRestaurantInfo() {
    const restaurantDetails = document.getElementById('restaurant-details');
    const buttonContainer = document.getElementById('buttonContainer');
    const jwt = localStorage.getItem("token");

    console.log("Fetching restaurant info...");

    fetch(`/restaurants/my-restaurant`, {
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
            throw new Error('No registered restaurant found.');
        }
    })
    .then(data => {
        // Restaurant name
        const name = document.createElement('h2');
        name.textContent = data.name;
        restaurantDetails.appendChild(name);

        // Cuisine type
        const cuisineType = document.createElement('h2');
        cuisineType.textContent = data.cuisineType;
        restaurantDetails.appendChild(cuisineType);

        // Restaurant img
        const image = document.createElement('img');
        image.src = data.restImage ? data.restImage : "/static/replace/replace_img.png";
        image.alt = data.restImage;
        image.classList.add('restaurant-image', 'mb-4');
        restaurantDetails.appendChild(image);

        // Create and show restaurant details
        const detailList = document.createElement('ul');
        detailList.classList.add('list-unstyled');
        restaurantDetails.appendChild(detailList);

        const detailItems = [
            { label: '주소', value: data.address },
            { label: '전화번호', value: data.phone },
            { label: '설명', value: data.description },
        ];

        detailItems.forEach(item => {
            const label = document.createElement('div');
            label.textContent = item.label;
            label.classList.add('detail-label');
            detailList.appendChild(label);

            const value = document.createElement('div');
            value.textContent = item.value;
            value.classList.add('detail-value');
            detailList.appendChild(value);
        });

        // Initialize map
        console.log("Initializing map with coordinates:", data.latitude, data.longitude);
        initMap(data.latitude, data.longitude);
    })
    .catch(error => {
        console.error('Fetch error:', error);
        restaurantDetails.textContent = 'Error connecting to the server. Details: ' + error.message;

        // Create "Create Restaurant" button if no restaurant is found
        restaurantDetails.textContent = '현재 등록된 식당이 없습니다.';
        buttonContainer.innerHTML =
            '<button class="btn btn-primary btn-restaurant" id="createRestaurantBtn">식당 생성</button>';
    });
}
fetchMyRestaurantInfo();

// Map function
function initMap(latitude, longitude) {
    const mapElement = document.getElementById('map');
    if (!mapElement) {
        console.error('Map container not found');
        return;
    }

    // Set the map options
    const mapOptions = {
        center: new naver.maps.LatLng(latitude, longitude),
        zoom: 15,
    };

    // Initialize the map
    const map = new naver.maps.Map(mapElement, mapOptions);

    // Create a polyline variable (if needed) and markers array
    let polyline = null;
    let markers = [];

    // Set up the click event listener for the map
    naver.maps.Event.addListener(map, 'click', function(e) {
        // Clear existing polyline if necessary
        if (polyline) {
            polyline.setMap(null);
        }

        // If there are already two markers, remove them
        if (markers.length === 2) {
            markers.forEach(marker => {
                marker.setMap(null);
            });
            markers.length = 0; // Clear the markers array
        } else {
            // Add a new marker at the clicked position
            const newMarker = new naver.maps.Marker({
                position: e.coord,
                map: map,
            });
            markers.push(newMarker); // Add new marker to the array
        }
    });
}