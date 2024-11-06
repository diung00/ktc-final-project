async function displayOrderDetails(orderId) {
    try {
        const response = await fetch(`/orders/${orderId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem("token")}`
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
            <p><strong>Order ID:</strong> ${orderData.id}</p>
<!--            <p><strong>Driver ID:</strong> ${orderData.driverId || 'N/A'}</p>-->
            <p><strong>Username:</strong> ${orderData.username}</p>
            <p><strong>Delivery Address:</strong> ${orderData.deliveryAddress}</p>
            <p><strong>Restaurant Name:</strong> ${orderData.restaurantName}</p>
            <p><strong>Restaurant Address:</strong> ${orderData.restaurantAddress}</p>
            <p><strong>Order Date:</strong> ${new Date(orderData.orderDate).toLocaleString()}</p>
            <p><strong>Order Status:</strong> ${orderData.orderStatus}</p>
            <p><strong>Total Menu Price:</strong> $${Number(orderData.totalMenusPrice).toFixed(2)}</p>
            <p><strong>Shipping Fee:</strong> $${Number(orderData.shippingFee).toFixed(2)}</p>
            <p><strong>Total Amount:</strong> $${Number(orderData.totalAmount).toFixed(2)}</p>
            <p><strong>Estimated Arrival Time:</strong> ${new Date(orderData.estimatedArrivalTime).toLocaleString()}</p>
        `;
            const userLatitude = orderData.userLatitude;
            const userLongitude = orderData.userLatitude;
            const restaurantLatitude = orderData.userLatitude;
            const restaurantLongitude = orderData.userLatitude;


            initMap(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude);

    } catch (error) {
        console.error('Error fetching order details:', error);
        alert('Could not load order details. Please try again later.\n' + error.message);
    }
}
function initMap(userLatitude, userLongitude, restaurantLatitude, restaurantLongitude) {
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
    const userMarker = new naver.maps.Marker({
        position: new naver.maps.LatLng(userLatitude, userLongitude),
        map: map,
        title: 'User Location'
    });
    console.log('User marker set at:', userLatitude, userLongitude);

    // Đặt marker tại vị trí nhà hàng
    const restaurantMarker = new naver.maps.Marker({
        position: new naver.maps.LatLng(restaurantLatitude, restaurantLongitude),
        map: map,
        title: 'Restaurant Location'
    });
    console.log('Restaurant marker set at:', restaurantLatitude, restaurantLongitude);
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
