async function displayOrderDetails(orderId) {
    try {
        const response = await fetch(`/orders/${orderId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("token")}`
            }
        });

        console.log('Response Status:', response.status); // Kiểm tra trạng thái

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error('Network response was not ok: ' + errorText);
        }

        const orderData = await response.json();
        console.log(orderData); // Kiểm tra dữ liệu nhận được

        document.getElementById('order-details').innerHTML = `
            <p><strong>Order ID:</strong> ${orderData.id}</p>
            <p><strong>Driver ID:</strong> ${orderData.driverId || 'N/A'}</p>
            <p><strong>User ID:</strong> ${orderData.userId}</p>
            <p><strong>Delivery Address:</strong> ${orderData.deliveryAddress}</p>
            <p><strong>Restaurant ID:</strong> ${orderData.restaurantId}</p>
            <p><strong>Order Date:</strong> ${new Date(orderData.orderDate).toLocaleString()}</p>
            <p><strong>Order Status:</strong> ${orderData.orderStatus}</p>
            <p><strong>Total Menu Price:</strong> ${orderData.totalMenusPrice.toFixed(2)}</p>
            <p><strong>Shipping Fee:</strong> ${orderData.shippingFee.toFixed(2)}</p>
            <p><strong>Total Amount:</strong> ${orderData.totalAmount.toFixed(2)}</p>
            <p><strong>Estimated Arrival Time:</strong> ${new Date(orderData.estimatedArrivalTime).toLocaleString()}</p>
        `;

        // Gọi hàm để lấy chỉ đường
        ///await getDirectionsFromOrder(orderData);
    } catch (error) {
        console.error('Error fetching order details:', error);
        alert('Could not load order details. Please try again later. \n' + error.message); // Hiển thị thông báo lỗi
    }
}


async function getDirectionsFromOrder(order) {
    const dto = await getCoordinatesFromOrder(order);
    try {
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
            throw new Error('Failed to get directions');
        }
    } catch (error) {
        console.error(error);
        alert('Could not get directions. Please try again later.');
    }
}

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
        title: 'Địa chỉ người dùng'
    });

    // Đánh dấu địa chỉ nhà hàng
    const restaurantMarker = new naver.maps.Marker({
        position: coordinates[coordinates.length - 1],
        map: map,
        title: 'Địa chỉ nhà hàng'
    });

    map.setCenter(coordinates[0]); // Trung tâm bản đồ tại điểm bắt đầu

    console.log('User Location:', coordinates[0]); // Log vị trí người dùng
    console.log('Restaurant Location:', coordinates[coordinates.length - 1]); // Log vị trí nhà hàng
}


// Khởi tạo bản đồ
const map = new naver.maps.Map('map', {
    center: new naver.maps.LatLng(37.5665, 126.978), // Tọa độ mặc định
    zoom: 10
});

async function getCoordinatesFromOrder(order) {
    return {
        start: await getCoordinates(order.deliveryAddress), // Lấy tọa độ người dùng
        goal: await getCoordinates(order.restaurant.address) // Lấy tọa độ nhà hàng
    };
}

async function getCoordinates(address) {
    const response = await fetch(`/navigate/geocode?query=${encodeURIComponent(address)}`); // Gọi API geocoding
    return await response.json();
}

// Đăng xuất
document.getElementById('logout-link').addEventListener('click', function(event) {
    event.preventDefault();
    localStorage.removeItem('token');
    window.location.href = '/views/login';
});

const urlParams = new URLSearchParams(window.location.search);
const orderId = urlParams.get('orderId');
displayOrderDetails(orderId);
