document.addEventListener("DOMContentLoaded", function() {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        alert("You must be logged in to access this page.");
        return;
    }

    // Map function
    function initMap() {
        var map = new naver.maps.Map('map', {
            center: new naver.maps.LatLng(37.3595704, 127.10599),
            zoom: 10 
        });
    }

    initMap();

    // Get driver status
    const statusButton = document.getElementById('availabilityBtn');

    // get driver status function
    fetch(`/drivers/status`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${jwt}`
        }
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error('Failed to fetch driver status');
        }
    })
    .then(status => {
        driverStatus = status;
        console.log('Driver status received:', driverStatus); 
        // update driver status depend on received status
        statusButton.setAttribute('data-status', status);
        statusButton.textContent = status === 'AVAILABLE' ? '운행하기' : '운행 중지'; 
    })
    .catch(error => {
        console.error('Error:', error);
        alert('An error occurred while fetching driver status');
    });

    // Update status button function (available mode)
    statusButton.addEventListener('click', function() {
        const newStatus = driverStatus === 'AVAILABLE' ? 'UNAVAILABLE' : 'AVAILABLE'; //set new status

        fetch(`/drivers/update-status?status=${newStatus}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${jwt}` 
            },
            body: JSON.stringify({status : newStatus})
        })
        .then(response => {
            if (response.ok) {
                alert(`Driver status set to ${newStatus}`); 

                // Update status
                driverStatus = newStatus;
                this.setAttribute('data-status', driverStatus); //update status
                this.textContent = newStatus === 'AVAILABLE' ? '운행하기' : '운행 중지'; //update content of button
            } else {
                alert('Error setting driver status'); 
            }
        })
        .catch(error => {
            console.error('Error:', error); 
            alert('An error occurred while changing driver status');
        });
    });

    // Redirect to orderList page
    document.getElementById('orderListBtn').addEventListener('click', function() {
        window.location.href = '/views/driver-orderList';
    });
});
