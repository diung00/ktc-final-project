document.addEventListener('DOMContentLoaded', fetchMyRequests);

function fetchMyRequests() {
    const jwt = localStorage.getItem("token");

    fetch('/restaurants/my-requests', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Unable to fetch requests.');
        }
        return response.json();
    })
    .then(requests => {
        const requestsTableBody = document.querySelector('#requests-table tbody');
        requestsTableBody.innerHTML = '';

        requests.forEach(request => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${request.id}</td>
                <td>${request.restaurantId}</td>
                <td>${request.restaurantName}</td>
                <td>${request.requestType}</td>
                <td>${request.status}</td>
                <td>${request.rejectionReason || 'N/A'}</td>
            `;

            requestsTableBody.appendChild(row);
        });
    })
    .catch(error => {
        console.error('Error fetching requests:', error);
        const requestsTableBody = document.querySelector('#requests-table tbody');
        requestsTableBody.innerHTML = `<tr><td colspan="6">Error fetching requests: ${error.message}</td></tr>`;
    });
}
