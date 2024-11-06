document.addEventListener("DOMContentLoaded", function() {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        alert("You must be logged in to access this page.");
        return;
    }

    let lastLatitude = null;
    let lastLongitude = null;
    const distanceThreshold = 0.1; // (0.1 km)
    let map, marker;

    // Get initial driver location from backend
    function getDriverLocation() {
        fetch('/drivers/location', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${jwt}`,
            }
        })
        .then(response => response.json())
        .then(location => {
            const latitude = location.latitude;
            const longitude = location.longitude;
            console.log("Initial driver location from backend:", latitude, longitude);

            // Initialize map if it's the first time
            if (!map) {
                map = new naver.maps.Map('map', {
                    center: new naver.maps.LatLng(latitude, longitude),
                    zoom: 14
                });
                marker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(latitude, longitude),
                    map: map,
                    icon: {
                        url: '/static/img/icon/map.png',  
                        size: new naver.maps.Size(32, 32),
                        anchor: new naver.maps.Point(12, 24)
                    }
                });
            }

            // if last data is null, update data
            if (lastLatitude === null || lastLongitude === null) {
                lastLatitude = latitude;
                lastLongitude = longitude;
                return;
            }

            // calculate distance
            const distance = calculateDistance(lastLatitude, lastLongitude, latitude, longitude);

            // if distance >= distanceThreshold (100m)
            if (distance >= distanceThreshold) {
                updateDriverLocation(latitude, longitude);
                lastLatitude = latitude;  // update location
                lastLongitude = longitude;

                // update position on map
                updateMapPosition(latitude, longitude);
            }
        })
        .catch(error => {
            console.error("Error getting location from backend:", error);
            alert("Could not retrieve location from server.");
        });
    }

    // calculate Distance (unit: km)
    function calculateDistance(lat1, lon1, lat2, lon2) {
        const R = 6371; //earth radius (km)
        const dLat = (lat2 - lat1) * Math.PI / 180;
        const dLon = (lon2 - lon1) * Math.PI / 180;
        const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                  Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                  Math.sin(dLon / 2) * Math.sin(dLon / 2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        const distance = R * c; // (km)
        return distance;
    }

    // send location to backend
    function updateDriverLocation(latitude, longitude) {
        console.log("update driver location...");
        const driverData = {
            latitude: latitude,
            longitude: longitude
        };

        fetch('/drivers/update-location', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${jwt}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(driverData)
        })
        .then(response => {
            if (response.ok) {
                console.log('Driver location updated successfully');
            } else {
                alert('Failed to update driver location');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while updating driver location');
        });
    }

    // update driver position on map
    function updateMapPosition(latitude, longitude) {
        marker.setPosition(new naver.maps.LatLng(latitude, longitude));
        map.setCenter(new naver.maps.LatLng(latitude, longitude));
    }

    // Function to get geolocation and update location on change
    function updateLocationFromGeo() {
        if (navigator.geolocation) {
            navigator.geolocation.watchPosition(
                function(position) {
                    const latitude = position.coords.latitude;
                    const longitude = position.coords.longitude;
                    console.log("Updated driver location from geolocation:", latitude, longitude);

                    // If last data is not null, check if the distance has changed
                    if (lastLatitude === null || lastLongitude === null) {
                        lastLatitude = latitude;
                        lastLongitude = longitude;
                        return;
                    }

                    // calculate distance
                    const distance = calculateDistance(lastLatitude, lastLongitude, latitude, longitude);

                    // if distance >= distanceThreshold (100m)
                    if (distance >= distanceThreshold) {
                        updateDriverLocation(latitude, longitude);
                        lastLatitude = latitude;  // update location
                        lastLongitude = longitude;

                        // update position on map
                        updateMapPosition(latitude, longitude);
                    }
                },
                function(error) {
                    console.error("Error getting geolocation:", error);
                    alert("Could not retrieve geolocation.");
                },
                {
                    enableHighAccuracy: true,  // Use high accuracy if possible
                    timeout: 5000,             // 5 seconds timeout
                    maximumAge: 0              // Don't use cached position
                }
            );
        } else {
            alert("Geolocation is not supported by this browser.");
        }
    }

    // Initially get driver location from backend
    getDriverLocation();

    // Start watching geolocation updates
    updateLocationFromGeo();

    // Set interval to get location updates from backend every 10 seconds
    setInterval(getDriverLocation, 10000);

    // Get driver status
    const statusButton = document.getElementById('availabilityBtn');
    let driverStatus = '';

    // get driver status function
    fetch(`/drivers/status`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Failed to fetch driver status');
        }
    })
    .then(status => {
        driverStatus = status;
        console.log('Driver status received:', driverStatus); 
        // update driver status depend on received status
        statusButton.setAttribute('data-status', status);
        statusButton.textContent = status === 'AVAILABLE' ? '운행하기' : '운행 중지'; 
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while fetching driver status');
    });

    // Update status button function (available mode)
    statusButton.addEventListener('click', function() {
        const newStatus = driverStatus === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE'; // set new status

        fetch(`/drivers/update-status?status=${newStatus}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${jwt}` 
            },
            body: JSON.stringify({status: newStatus})
        })
        .then(response => {
            if (response.ok) {
                alert(`Driver status set to ${newStatus}`); 

                // Update status
                driverStatus = newStatus;
                this.setAttribute('data-status', driverStatus); // update status
                this.textContent = newStatus === 'AVAILABLE' ? '운행하기' : '운행 중지'; // update content of button
            } else {
                alert('Error setting driver status'); 
            }
        })
        .catch(error => {
            console.error('Error:', error); 
            alert('An error occurred while changing driver status');
        });
    });

    // Redirect to orderList page
    document.getElementById('orderListBtn').addEventListener('click', function() {
        window.location.href = '/views/driver-orderList';
    });
});
