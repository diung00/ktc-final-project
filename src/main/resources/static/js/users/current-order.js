async function displayOrderDetails(orderId) {
    try {
        const response = await fetch(`orders/${orderId}`); // Gọi API để lấy thông tin đơn hàng
        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }
        const orderData = await response.json();

        // Cập nhật các thẻ span với thông tin đơn hàng
        document.getElementById('order-id').textContent = orderData.id;
        document.getElementById('driver-id').textContent = orderData.driverId;
        document.getElementById('user-id').textContent = orderData.userId;
        document.getElementById('delivery-address').textContent = orderData.deliveryAddress;
        document.getElementById('restaurant-id').textContent = orderData.restaurantId;
        document.getElementById('order-date').textContent = new Date(orderData.orderDate).toLocaleString();
        document.getElementById('order-status').textContent = orderData.orderStatus;
        document.getElementById('total-menus-price').textContent = orderData.totalMenusPrice.toFixed(2);
        document.getElementById('shipping-fee').textContent = orderData.shippingFee.toFixed(2);
        document.getElementById('total-amount').textContent = orderData.totalAmount.toFixed(2);
        document.getElementById('estimated-arrival-time').textContent = new Date(orderData.estimatedArrivalTime).toLocaleString();
    } catch (error) {
        console.error('Error fetching order details:', error);
    }
}

// Lấy orderId từ URL
const pathSegments = window.location.pathname.split('/');
const orderId = pathSegments[pathSegments.length - 1];

displayOrderDetails(orderId);

// Lấy tọa độ từ đơn hàng và vẽ đường đi
async function getDirectionsFromOrder(order) {
    try {
        const dto = await getCoordinatesFromOrder(order);

        const response = await fetch(`/navigate/points`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dto) // Gửi tọa độ
        });

        if (response.ok) {
            const data = await response.json();
            drawRoute(data.path);
        } else {
            console.error('Failed to get directions');
        }
    } catch (error) {
        console.error('Error getting directions:', error);
    }
}

// Vẽ đường đi trên bản đồ
function drawRoute(path) {
    const coordinates = path.map(point => new naver.maps.LatLng(point.lat, point.lng));
    const line = new naver.maps.Polyline({
        path: coordinates,
        strokeColor: '#FF0000',
        strokeWeight: 4,
        map: map
    });

    // Đánh dấu địa chỉ người dùng
    const userMarker = new naver.maps.Marker({
        position: coordinates[0],
        map: map,
        title: 'User Address'
    });

    // Đánh dấu địa chỉ nhà hàng
    const restaurantMarker = new naver.maps.Marker({
        position: coordinates[coordinates.length - 1],
        map: map,
        title: 'Restaurant Address'
    });

    map.setCenter(coordinates[0]); // Trung tâm bản đồ tại điểm bắt đầu
}

// Khởi tạo bản đồ
const map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.5665, 126.978), // Tọa độ mặc định
    zoom: 10
});

// Lấy tọa độ người dùng và nhà hàng từ đơn hàng
async function getCoordinatesFromOrder(order) {
    try {
        const response = await fetch(`orders/${order.id}`); // Lấy thông tin đơn hàng bằng orderId
        const orderData = await response.json();

        return {
            start: await getCoordinates(orderData.user.address), // Lấy tọa độ người dùng
            goal: await getCoordinates(orderData.restaurant.address) // Lấy tọa độ nhà hàng
        };
    } catch (error) {
        console.error('Error getting coordinates from order:', error);
    }
}

// Gọi API geocoding để lấy tọa độ
async function getCoordinates(address) {
    try {
        const response = await fetch(`/navigate/geocode?query=${encodeURIComponent(address)}`); // Gọi API geocoding
        return await response.json();
    } catch (error) {
        console.error('Error fetching coordinates:', error);
    }
}

// Đăng xuất
document.getElementById('logout-link').addEventListener('click', function(event) {
    event.preventDefault();
    localStorage.removeItem('token');
    window.location.href = '/views/login';
});
