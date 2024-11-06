document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("driver-registration-form");

    form.addEventListener("submit", async function (event) {
        event.preventDefault(); // Ngăn hành động submit mặc định

//        const username = document.getElementById("username").value;
//        const password = document.getElementById("password").value;
//        const confirmPassword = document.getElementById("confirmPassword").value;
//        const email = document.getElementById("email").value;
//        const phone = document.getElementById("phone").value;
//        const address = document.getElementById("address").value;
        const licenseNumber = document.getElementById("licenseNumber").value;

        // Kiểm tra mật khẩu và xác nhận mật khẩu
        if (password !== confirmPassword) {
            alert("Password and Confirm Password do not match.");
            return;
        }

        const data = {
//            username,
//            password,
//            confirmPassword,
//            email,
//            phone,
//            address,
            licenseNumber,
        };

        try {
            const response = await fetch("/api/drivers/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                // Xử lý khi đăng ký thành công
                alert("Registration successful! You can now log in.");
                form.reset(); // Reset form sau khi đăng ký thành công
            } else {
                // Xử lý lỗi từ server
                const errorData = await response.json();
                alert(`Registration failed: ${errorData.message || "Unknown error"}`);
            }
        } catch (error) {
            console.error("Error:", error);
            alert("An error occurred during registration. Please try again later.");
        }
    });
});
