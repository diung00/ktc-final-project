let selectedOrderId = null;

function fetchOrders() {
    const jwt = localStorage.getItem('token');

    if (!jwt) {
        alert('Please login to continue');
        location.href = '/views/login';
        return;
    }

    fetch('/orders/restaurant-orders', {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => response.json())
    .then(orders => {
        const ordersTableBody = document.getElementById('ordersTableBody');
        ordersTableBody.innerHTML = ''; // Clear existing rows

        orders.sort((a, b) => b.id - a.id); // Sort by ID descending

        if (orders.length === 0) {
            ordersTableBody.innerHTML = `<tr><td colspan="2" style="text-align: center;">No orders here.</td></tr>`;
        } else {
            orders.forEach(order => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${order.id}</td>
                    <td>${order.orderStatus}</td>
                `;
                row.onclick = () => showOrderDetails(order); // Show details on click
                ordersTableBody.appendChild(row);
            });
        }
    })
    .catch(error => console.error('Error fetching orders:', error));
}

function showOrderDetails(order) {
    selectedOrderId = order.id;
    const orderInfo = document.getElementById('orderInfo');
    orderInfo.innerHTML = `
        <div><strong>Restaurant:</strong> ${order.restaurantName}</div>
        <div><strong>Customer:</strong> ${order.username}</div>
        <div><strong>Delivery Address:</strong> ${order.deliveryAddress}</div>
        <div><strong>Total:</strong> $${order.totalAmount.toFixed(2)}</div>
        <div><strong>Status:</strong> ${order.orderStatus}</div>
        <div><strong>Estimated Arrival:</strong> ${order.estimatedArrivalTime ? new Date(order.estimatedArrivalTime).toLocaleString() : 'N/A'}</div>
    `;
}

function approveOrder(orderId) {
    if (!orderId) {
        alert('Please select an order to approve.');
        return;
    }

    const jwt = localStorage.getItem('token');
    fetch(`/orders/${orderId}/approve`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => {
        if (response.ok) {
            alert('Order approved successfully.');
            fetchOrders(); // Refresh the order list
            document.getElementById('orderInfo').innerHTML = 'Select an order to view details'; // Clear order details
        } else {
            alert('Failed to approve the order.');
        }
    })
    .catch(error => console.error('Error approving order:', error));
}

function findDriver(orderId) {
    if (!orderId) {
        alert('Please select an order to find a driver for.');
        return;
    }

    const jwt = localStorage.getItem('token');
    fetch(`/orders/${orderId}/find-driver`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => {
        if (response.ok) {
            alert('Driver found successfully.');
            fetchOrders(); // Refresh the order list
            document.getElementById('orderInfo').innerHTML = 'Select an order to view details'; // Clear order details
        } else {
            alert('Failed to find a driver for the order.');
        }
    })
    .catch(error => console.error('Error finding driver:', error));
}

function cancelOrder(orderId) {
    if (!orderId) {
        alert('Please select an order to cancel.');
        return;
    }

    const jwt = localStorage.getItem('token');
    fetch(`/orders/${orderId}/cancel`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => {
        if (response.ok) {
            alert('Order canceled successfully.');
            fetchOrders(); // Refresh the order list
            document.getElementById('orderInfo').innerHTML = 'Select an order to view details'; // Clear order details
        } else {
            alert('Failed to cancel the order.');
        }
    })
    .catch(error => console.error('Error canceling order:', error));
}

// Assign button actions on DOMContentLoaded
document.addEventListener('DOMContentLoaded', () => {
    fetchOrders();

    // Assign action buttons to functions
    document.querySelector('.approve').addEventListener('click', () => approveOrder(selectedOrderId));
    document.querySelector('.find-driver').addEventListener('click', () => findDriver(selectedOrderId));
    document.querySelector('.cancel').addEventListener('click', () => cancelOrder(selectedOrderId));
});