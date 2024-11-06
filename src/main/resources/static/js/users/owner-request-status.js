document.addEventListener('DOMContentLoaded', () => {
    const jwt = localStorage.getItem("token");
    if (!jwt) {
        location.href = "/views/login";
        return;
    }
});

// Kiểm tra xem người dùng đã đăng nhập chưa
if (!jwt) {
    location.href = "/views/login"; // Chuyển hướng đến trang đăng nhập nếu không có token
}

// Gọi API để lấy thông tin trạng thái yêu cầu chủ sở hữu
fetch("/users/owner-request-status", {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${jwt}` // Thêm token vào header
    },
})
    .then(response => {
        // Kiểm tra phản hồi từ server
        if (response.ok) {
            return response.json(); // Chuyển đổi phản hồi thành JSON
        } else if (response.status === 403) {
            localStorage.removeItem("token"); // Xóa token nếu bị từ chối quyền truy cập
            location.href = "/views/login"; // Chuyển hướng đến trang đăng nhập
        } else {
            throw new Error("Failed to fetch owner request status."); // Ném lỗi nếu phản hồi không thành công
        }
    })
    .then(json => {
        // Kiểm tra nếu không có yêu cầu trạng thái
        if (!json || Object.keys(json).length === 0) {
            document.body.innerHTML = '<h3>No owner request status found</h3>'; // Hiển thị thông báo khi không có yêu cầu
            return;
        }

        // Cập nhật nội dung của các thẻ h3 với thông tin từ JSON
        document.getElementById("businessNumber").innerHTML = `Business Number: <span class="text-muted">${json.businessNumber || 'N/A'}</span>`;
        document.getElementById("userRole").innerHTML = `User Role: <span class="text-muted">${json.userRole || 'N/A'}</span>`;
        document.getElementById("status").innerHTML = `Status: <span class="text-muted">${json.status || 'N/A'}</span>`;
        document.getElementById("rejectionReason").innerHTML = `Rejection Reason: <span class="text-muted">${json.rejectionReason || 'N/A'}</span>`; // Hiển thị 'N/A' nếu không có lý do từ chối
    })
    .catch(e => {
        alert(e.message); // Hiển thị thông báo lỗi nếu có
    });
