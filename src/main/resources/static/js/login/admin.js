const userTableBody = document.getElementById('user-table').getElementsByTagName('tbody')[0];
const ownerRequestTableBody = document.getElementById('owner-request-table').getElementsByTagName('tbody')[0];
const driverRequestTableBody = document.getElementById('driver-request-table').getElementsByTagName('tbody')[0];
const restaurantRequestTableBody = document.getElementById('restaurant-request-table').getElementsByTagName('tbody')[0];

const jwtToken = localStorage.getItem("token");
const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${jwtToken}`
};

function fetchUsers() {
    fetch('/admin/users', { headers })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                console.error("Error loading user list");
                throw new Error("Error loading user list");
            }
        })
        .then(users => populateUserTable(users))
        .catch(error => console.error("Error fetching users:", error));
}

function populateUserTable(users) {
    userTableBody.innerHTML = '';
    users.forEach(user => {
        const row = userTableBody.insertRow();
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.role}</td>
        `;
    });
}

function fetchOwnerRequests() {
    fetch('/admin/owner-requests', { headers })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                console.error("Error loading request list");
                throw new Error("Error loading request list");
            }
        })
        .then(requests => populateOwnerRequestTable(requests))
        .catch(error => console.error("Error fetching owner requests:", error));
}

function populateOwnerRequestTable(requests) {
    ownerRequestTableBody.innerHTML = '';
    requests.forEach(request => {
        const row = ownerRequestTableBody.insertRow();
        row.innerHTML = `
            <td>${request.id}</td>
            <td>${request.userId}</td>
            <td>
                <button onclick="acceptRequest(${request.id}, 'Owner')">Accept</button>
                <button onclick="declineRequest(${request.id}, 'Owner')">Deny</button>
            </td>
        `;
    });
}

function fetchDriverRequests() {
    fetch('/admin/driver-requests', { headers })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                console.error("Error loading driver request");
                throw new Error("Error loading driver request");
            }
        })
        .then(requests => populateDriverRequestTable(requests))
        .catch(error => console.error("Error fetching driver requests:", error));
}

function populateDriverRequestTable(requests) {
    driverRequestTableBody.innerHTML = '';
    requests.forEach(request => {
        const row = driverRequestTableBody.insertRow();
        row.innerHTML = `
            <td>${request.id}</td>
            <td>${request.userId}</td>
            <td>
                <button onclick="acceptRequest(${request.id}, 'Driver')">Accept</button>
                <button onclick="declineRequest(${request.id}, 'Driver')">Deny</button>
            </td>
        `;
    });
}

function fetchRestaurantRequests() {
    fetch('/admin/restaurant-requests/pending', { headers })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                console.error("Error loading restaurant requests");
                throw new Error("Error loading restaurant requests");
            }
        })
        .then(requests => populateRestaurantRequestTable(requests))
        .catch(error => console.error("Error fetching restaurant requests:", error));
}

function populateRestaurantRequestTable(requests) {
    restaurantRequestTableBody.innerHTML = '';
    requests.forEach(request => {
        const row = restaurantRequestTableBody.insertRow();
        row.innerHTML = `
            <td>${request.id}</td>
            <td>${request.restaurantName}</td>
            <td>${request.status}</td>
            <td>
                <button onclick="approveRestaurantRequest(${request.id}, 'open')">Open</button>
                <button onclick="approveRestaurantRequest(${request.id}, 'close')">Close</button>
                <button onclick="declineRestaurantRequest(${request.id})">Deny</button>
            </td>
        `;
    });
}

function acceptRequest(requestId, roleType) {
    const endpoint = `/admin/${roleType.toLowerCase()}-requests/${requestId}/accept`;
    fetch(endpoint, { method: 'POST', headers })
        .then(response => {
            if (response.ok) {
                alert(`${roleType} request accepted successfully.`);
                roleType === 'Owner' ? fetchOwnerRequests() : fetchDriverRequests();
            } else {
                alert("Failed to accept request.");
            }
        })
        .catch(error => console.error("Error accepting request:", error));
}

function declineRequest(requestId, roleType) {
    const reason = prompt("Deny reason: ");
    const endpoint = `/admin/${roleType.toLowerCase()}-requests/${requestId}/decline`;
    fetch(endpoint, {
        method: 'POST',
        headers,
        body: JSON.stringify(reason)
    })
        .then(response => {
            if (response.ok) {
                alert(`${roleType} request declined successfully.`);
                roleType === 'Owner' ? fetchOwnerRequests() : fetchDriverRequests();
            } else {
                alert("Failed to decline request.");
            }
        })
        .catch(error => console.error("Error declining request:", error));
}

function approveRestaurantRequest(requestId, action) {
    const endpoint = `/admin/restaurant-requests/${requestId}/approve/${action}`;
    fetch(endpoint, { method: 'POST', headers })
        .then(response => {
            if (response.ok) {
                alert(`Restaurant ${action} approved successfully.`);
                fetchRestaurantRequests();
            } else {
                alert("Failed to approve restaurant request.");
            }
        })
        .catch(error => console.error(`Error approving restaurant ${action}:`, error));
}

function declineRestaurantRequest(requestId) {
    const reason = prompt("Deny reason: ");
    const endpoint = `/admin/restaurant-requests/${requestId}/decline`;
    fetch(endpoint, {
        method: 'POST',
        headers,
        body: JSON.stringify(reason)
    })
        .then(response => {
            if (response.ok) {
                alert("Restaurant request declined successfully.");
                fetchRestaurantRequests();
            } else {
                alert("Failed to decline restaurant request.");
            }
        })
        .catch(error => console.error("Error declining restaurant request:", error));
}
