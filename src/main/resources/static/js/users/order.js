
const orderData = JSON.parse(sessionStorage.getItem('orderData'));

if (orderData) {
    const orderSummary = document.getElementById('order-summary');
    orderSummary.innerHTML = `
                <p><strong>User ID:</strong> ${orderData.userId}</p>
                <p><strong>Delivery Address:</strong> ${orderData.deliveryAddress}</p>
                <p><strong>Restaurant ID:</strong> ${orderData.restaurantId}</p>
                <p><strong>Order Date:</strong> ${new Date(orderData.orderDate).toLocaleString()}</p>
                <p><strong>Order Status:</strong> ${orderData.orderStatus}</p>
                <p><strong>Total Menu Price:</strong> $${orderData.totalMenusPrice.toFixed(2)}</p>
                <p><strong>Shipping Fee:</strong> $${orderData.shippingFee.toFixed(2)}</p>
                <p><strong>Total Amount:</strong> $${orderData.totalAmount.toFixed(2)}</p>
                <p><strong>Estimated Arrival Time:</strong> ${new Date(orderData.estimatedArrivalTime).toLocaleString()}</p>
            `;
    document.getElementById('confirm-order').addEventListener('click', () => {
        createOrder(orderData)
            .then(response => {

                sessionStorage.setItem('orderId', response.id);
                window.location.href = `/views/current-order?orderId=${response.id}`;
            })
            .catch(error => {
                console.error('Error creating order:', error);
                alert('Failed to create order. Please try again.');
            });
    });

} else {
    document.getElementById('order-summary').innerHTML = '<p>No order data found.</p>';
}

function createOrder(orderData) {
    const jwt = localStorage.getItem("token");
    return fetch('/orders/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwt}`
        },
        body: JSON.stringify(orderData)
    }).then(response => {
        if (response.ok) {
            return response.json();
        } else {
            throw new Error('Unable to create order.');
        }
    });
}