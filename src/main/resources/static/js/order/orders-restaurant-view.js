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
        const ordersTableBody = document.getElementById('ordersTable').getElementsByTagName('tbody')[0];
        ordersTableBody.innerHTML = ''; // Clear existing rows

        if (orders.length === 0) {
            // Display message if no orders
            const noOrderRow = document.createElement('tr');
            noOrderRow.innerHTML = `
                <td colspan="3" style="text-align: center; padding: 20px; color: #888;">
                    No orders here.
                </td>
            `;
            ordersTableBody.appendChild(noOrderRow);
        } else {
            orders.forEach(order => {
                const row = document.createElement('tr');
                
                row.innerHTML = `
                    <td>${order.id}</td>
                    <td class="order-info">
                        <div><strong>Restaurant:</strong> ${order.restaurantName}</div>
                        <div><strong>Customer:</strong> ${order.username}</div>
                        <div><strong>Delivery Address:</strong> ${order.deliveryAddress}</div>
                        <div><strong>Total:</strong> $${order.totalAmount.toFixed(2)}</div>
                        <div><strong>Status:</strong> ${order.orderStatus}</div>
                        <div><strong>Estimated Arrival:</strong> ${order.estimatedArrivalTime ? new Date(order.estimatedArrivalTime).toLocaleString() : 'N/A'}</div>
                    </td>
                    <td class="action-buttons">
                        <button class="approve" onclick="approveOrder(${order.id})">Approve</button>
                        <button class="find-driver" onclick="findDriver(${order.id})">Find Driver</button>
                        <button class="cancel" onclick="cancelOrder(${order.id})">Cancel</button>
                    </td>
                `;
    
                ordersTableBody.appendChild(row);
            });
        }

    })
    .catch(error => console.error('Error fetching orders:', error));
}

// Function to find driver for the order
function findDriver(orderId) {
    fetch(`/orders/find-driver/${orderId}`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => response.json())
    .then(updatedOrder => {
        alert(`Driver assigned for Order ${updatedOrder.id}.`);
        fetchOrders(); // Refresh the orders list
    })
    .catch(error => console.error('Error finding driver:', error));
}

// Function to cancel an order
function cancelOrder(orderId) {
    fetch(`/orders/restaurant/${orderId}/cancel`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => response.text())
    .then(message => {
        alert(message);
        fetchOrders(); // Refresh the orders list
    })
    .catch(error => console.error('Error canceling order:', error));
}

// Initialize the page by fetching the orders
document.addEventListener('DOMContentLoaded', fetchOrders);
