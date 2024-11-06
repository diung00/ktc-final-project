const jwt = localStorage.getItem("token");
if (!jwt){
    location.href = "/views/login";
}

async function displayOrderDetails(orderId) {

    try {
        const response = await fetch(`/orders/${orderId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            }
        });

        console.log('Response Status:', response.status);

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error('Network response was not ok: ' + errorText);
        }

        const orderData = await response.json();
        console.log(orderData);

        document.getElementById('order-details').innerHTML = `
<!--            <p><strong>Order ID:</strong> ${orderData.id}</p>-->
<!--            <p><strong>Driver ID:</strong> ${orderData.driverId || 'N/A'}</p>-->
            <p><strong>Username:</strong> ${orderData.username}</p>
            <p><strong>주소:</strong> ${orderData.deliveryAddress}</p>
            <p><strong>식당 이름:</strong> ${orderData.restaurantName}</p>
            <p><strong>식당 주소:</strong> ${orderData.restaurantAddress}</p>
            <p><strong>주문 날자:</strong> ${new Date(orderData.orderDate).toLocaleString()}</p>
            <p><strong>주문 상태:</strong> ${orderData.orderStatus}</p>
            <p><strong>주문 금액:</strong> $${Number(orderData.totalMenusPrice).toFixed(2)}</p>
            <p><strong>배달비:</strong> $${Number(orderData.shippingFee).toFixed(2)}</p>
            <p><strong>총 금액:</strong> $${Number(orderData.totalAmount).toFixed(2)}</p>
            <p><strong>도착 예정 시간:</strong> ${new Date(orderData.estimatedArrivalTime).toLocaleString()}</p>
        `;
            const userLatitude = orderData.userLatitude;
            const userLongitude = orderData.userLongitude;
            const restaurantLatitude = orderData.restaurantLatitude;
            const restaurantLongitude = orderData.restaurantLongitude;


            initMap(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude);

    } catch (error) {
        console.error('Error fetching order details:', error);
        alert('Could not load order details. Please try again later.\n' + error.message);
    }
}

async function fetchRoute(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude) {
    try {
        const response = await fetch('/navigate/points', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwt}`
            },
            body: JSON.stringify({
                start: {
                    latitude: userLatitude,  // Tọa độ bắt đầu (người dùng)
                    longitude: userLongitude
                },
                goal: {
                    latitude: restaurantLatitude,  // Tọa độ đích (nhà hàng)
                    longitude: restaurantLongitude
                }
            })
        });

        if (!response.ok) {
            throw new Error('Failed to fetch route');
        }

        const routeData = await response.json(); // Nhận dữ liệu đường đi từ backend
        console.log('Route data:', routeData);
        return routeData;
    } catch (error) {
        console.error('Error fetching route:', error);
        return null;
    }
}


async function initMap(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude) {
    // Kiểm tra xem phần tử chứa bản đồ có tồn tại hay không
    const mapElement = document.getElementById('map');
    if (!mapElement) {
        console.error('Map container not found');
        return;
    }

    // Khởi tạo bản đồ với tọa độ trung tâm là tọa độ người dùng
    const map = new naver.maps.Map(mapElement, {
        center: new naver.maps.LatLng(userLatitude, userLongitude),
        zoom: 15,
        mapTypeId: naver.maps.MapTypeId.NORMAL
    });

    console.log('Map initialized with center:', userLatitude, userLongitude);

    // Đặt marker tại vị trí người dùng
    const iconUrl = '/static/img/icon/map.png';
    const userMarker = new naver.maps.Marker({
        position: new naver.maps.LatLng(userLatitude, userLongitude),
        map: map,
        icon: {
            url: iconUrl,
            size: new naver.maps.Size(32, 32),
            origin: new naver.maps.Point(0, 0),
            anchor: new naver.maps.Point(16, 32)
        }

    });
    console.log('User marker set at:', userLatitude, userLongitude);

    // Đặt marker tại vị trí nhà hàng
    const restaurantMarker = new naver.maps.Marker({
        position: new naver.maps.LatLng(restaurantLatitude, restaurantLongitude),
        map: map

    });
    console.log('Restaurant marker set at:', restaurantLatitude, restaurantLongitude);

    // Lấy dữ liệu đường đi từ backend
    const routeData = await fetchRoute( restaurantLatitude, restaurantLongitude, userLatitude, userLongitude);
    if (!routeData) {
        console.error('No route data available');
        return;
    }
    console.log(Array.isArray(routeData));
    // Tạo đường đi từ dữ liệu trả về
    if (routeData && Array.isArray(routeData.path)) {
        const path = routeData.path.map(point => new naver.maps.LatLng(point.latitude, point.longitude));

        const polyline = new naver.maps.Polyline({
            path: path,
            map: map,
            strokeColor: '#007AFF',
            strokeWeight: 4,
            strokeOpacity: 0.8
        });
    }

}

// Đăng xuất
document.getElementById('logout-link').addEventListener('click', function(event) {
    event.preventDefault();
    localStorage.removeItem('token');
    window.location.href = '/views/login';
});

// Lấy orderId từ URL và hiển thị chi tiết đơn hàng
const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get('orderId');
displayOrderDetails(orderId);
