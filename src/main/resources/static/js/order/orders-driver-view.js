document.addEventListener('DOMContentLoaded', () => {
    // Get all orders by driver function
    const fetchOrders = async () => {
        try {
            
            const jwt = localStorage.getItem('token');
            
            if (!jwt) {
                alert('Please login to continue');
                location.href = '/views/login';
                return;
            }

            const response = await fetch('/orders/driver', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${jwt}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Unable to Load Orders');
            }

            const orders = await response.json();
            renderOrders(orders);
        } catch (error) {
            console.error('Error while fetching data:', error);
        }
    };

    // Function to display order list on table
    const renderOrders = (orders) => {
        const tableBody = document.querySelector('#ordersTable tbody');
        tableBody.innerHTML = ''; 

        if (orders.length === 0) {
            tableBody.innerHTML = '<tr><td colspan="10">주문이 없습니다.</td></tr>';
        } else {
            orders.forEach(order => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${order.id}</td>
                    <td>${order.username}</td>
                    <td>${order.deliveryAddress}</td>
                    <td>${order.restaurantName}</td>
                    <td>${order.restaurantAddress}</td>
                    <td>${new Date(order.orderDate).toLocaleString()}</td>
                    <td>${order.orderStatus}</td>
                    <td>${new Date(order.estimatedArrivalTime).toLocaleString()}</td>
                `;
                tableBody.appendChild(row);
            });
        }
    };

    fetchOrders(); 
});
