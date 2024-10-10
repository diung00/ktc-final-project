package com.example.ChoiGangDeliveryApp.user.emailVerification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String toEmail, String token) {
        String subject = "Email Verification";
        String verificationLink = "http://localhost:8080/email/verify-email?token=" + token;
        String message = "Please click the link below to verify your email:\n" + verificationLink;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("no-reply@localhost");

        mailSender.send(mailMessage);
    }
}
