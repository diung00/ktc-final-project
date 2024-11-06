document.addEventListener('DOMContentLoaded', () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
        return;
    }
});

document.getElementById('request-driver-role-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const licenseNumber = document.getElementById('licenseNumber').value;

    if (!licenseNumber) {
        alert("Vui lòng nhập số bằng lái xe.");
        return;
    }

    const url = `/request-driver-role?licenseNumber=${encodeURIComponent(licenseNumber)}`;

    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}` // Giả sử bạn lưu JWT trong localStorage
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Request failed with status: ' + response.status);
        }
        return response.text(); // Nhận phản hồi dạng text
    })
    .then(data => {
        alert(data); // Hiển thị thông báo thành công
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Đã xảy ra lỗi trong khi yêu cầu nâng cấp vai trò tài xế.');
    });
});
