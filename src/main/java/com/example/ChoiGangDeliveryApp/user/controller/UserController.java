package com.example.ChoiGangDeliveryApp.user.controller;

import com.example.ChoiGangDeliveryApp.common.exception.GlobalException;
import com.example.ChoiGangDeliveryApp.jwt.JwtTokenUtils;
import com.example.ChoiGangDeliveryApp.jwt.dto.JwtRequestDto;
import com.example.ChoiGangDeliveryApp.jwt.dto.JwtResponseDto;
import com.example.ChoiGangDeliveryApp.user.dto.PasswordChangeRequestDto;
import com.example.ChoiGangDeliveryApp.user.dto.PasswordDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserCreateDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    JwtTokenUtils tokenUtils;
    // create a new user
    //role default is INACTIVE
    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody
            UserCreateDto dto
    ){
        try {
            UserDto createdUser = service.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Sign up successful. User created with email: " + createdUser.getEmail());
        } catch (GlobalException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sign up failed: " + e.getMessage());
        }
    }

    //verify email to upgrade role
    @PostMapping("/signup/verify")
    public ResponseEntity<String> verifyEmail (
            @RequestParam("email")
            String email,
            @RequestParam("code")
            String code
    ){
        service.verifyEmail(email, code);
        return ResponseEntity.ok("Verified successfully");
    }

    // log in
    @PostMapping("/login")
    public JwtResponseDto login (
            @RequestBody
            JwtRequestDto dto
    ){
        return service.login(dto);
    }

    // Send verification code for password reset
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            service.passwordSendCode(email);
            return ResponseEntity.ok("Password reset code sent to email: " + email);
        } catch (GlobalException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to send reset code: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<String> resetPassword (
            @RequestBody()
            PasswordChangeRequestDto requestDto
    ){
        try {
            service.resetPassword(requestDto);
            return ResponseEntity.ok("Password reset successful.");
        } catch (GlobalException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password reset failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword (
            @RequestBody()
            PasswordDto dto
    ){
         return service.changePassword(dto);
    }

    @PostMapping("/upload-profile-image")
    public void uploadProfileImage (
            @RequestParam("image")
            MultipartFile image
    )throws Exception {
        service.uploadProfileImage(image);
    }

    @GetMapping("/get-my-profile")
    public UserDto getMyProfile() {
        return service.getMyProfile();
    }

    @PostMapping("/request-owner-role")
    public void requestOwnerRole(
            @RequestParam("businessNumber")
            String businessNumber
    ){
        service.requestOwnerRole(businessNumber);
    }

    @PostMapping("/request-driver-role")
    public void requestDriverRole(
            @RequestParam("licenseNumber")
            String licenseNumber
    ){
        service.requestDriverRole(licenseNumber);
    }

    @GetMapping("/validate")
    public String validateTest(
            @RequestParam("token")
            String token
    ) {
        if (!tokenUtils.validate(token))
            return "not valid jwt";
        return "valid jwt";
    }


}
