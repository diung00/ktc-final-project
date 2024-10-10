package com.example.ChoiGangDeliveryApp.user.emailVerification;

import com.example.ChoiGangDeliveryApp.enums.VerificationStatus;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("email")
public class EmailController {
    @Autowired
    private EmailVerificationTokenRepo emailVerificationTokenRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(
            @RequestParam("token")
            String token
    ) {
        // Tìm token theo mã xác thực
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByVerifyCode(token);

        // Kiểm tra token có hợp lệ không
        if (verificationToken == null || verificationToken.getStatus() == VerificationStatus.VERIFIED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or already used token");
        }

        // Cập nhật trạng thái người dùng
        UserEntity user = verificationToken.getUser();
        user.setEmailVerified(true);  // Đánh dấu email đã xác thực
        userRepository.save(user);

        // Cập nhật trạng thái của token
        verificationToken.setStatus(VerificationStatus.VERIFIED);  // Đánh dấu token đã sử dụng
        emailVerificationTokenRepository.save(verificationToken);

        return ResponseEntity.ok("Email verified successfully");

    }

}
