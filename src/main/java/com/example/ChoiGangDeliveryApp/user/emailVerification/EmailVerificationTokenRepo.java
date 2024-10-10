package com.example.ChoiGangDeliveryApp.user.emailVerification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationTokenRepo extends JpaRepository<EmailVerificationToken, Long> {
    EmailVerificationToken findByVerifyCode(String verifyCode);
}
