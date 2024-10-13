package com.example.ChoiGangDeliveryApp.user.controller;

import com.example.ChoiGangDeliveryApp.jwt.JwtTokenUtils;
import com.example.ChoiGangDeliveryApp.jwt.dto.JwtRequestDto;
import com.example.ChoiGangDeliveryApp.jwt.dto.JwtResponseDto;
import com.example.ChoiGangDeliveryApp.user.dto.PasswordChangeRequestDto;
import com.example.ChoiGangDeliveryApp.user.dto.PasswordDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserCreateDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.service.UserService;
import io.jsonwebtoken.Jwt;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService service;
    JwtTokenUtils tokenUtils;

    // class này chỉ dùng cho signup và login

    // sign up
    @PostMapping("signup")
    public UserDto signup(
            @RequestBody
            UserCreateDto dto
    ){
       return service.createUser(dto);
    }

    // log in
    @PostMapping("login")
    public JwtResponseDto login (
            @RequestBody
            JwtRequestDto dto
    ){
        return service.login(dto);
    }

    @PostMapping("sendVerifyCode")
    public void sendVerifyCode (
            @RequestBody()
            String receiverEmail
    ){
        service.sendVerifyCode(receiverEmail);
    }

    @PostMapping("verifyEmail")
    public void verifyEmail (
            @RequestBody()
            String email,
            String code
    ){
        service.verifyEmail(email, code);
    }



    @PostMapping("signUpSendCode")
    public void signUpSendCode (
            @RequestBody()
            String email
    ){
        service.signUpSendCode(email);
    }


    @PostMapping("passwordSendCode")
    public ResponseEntity<String> passwordSendCode (
            @RequestBody()
            String email
    ){
         return service.passwordSendCode(email);
    }


    @PostMapping("resetPassword")
    public ResponseEntity<String> resetPassword (
            @RequestBody()
            PasswordChangeRequestDto requestDto
    ){
         return service.resetPassword(requestDto);
    }

    @PostMapping("changePassword")
    public ResponseEntity<String> changePassword (
            @RequestBody()
            PasswordDto dto
    ){
         return service.changePassword(dto);
    }

    @PostMapping("/uploadProfileImage")
    public void uploadProfileImage (
            @RequestParam("image")
            MultipartFile image
    )throws Exception {
        service.uploadProfileImage(image);
    }

    @GetMapping("/getMyProfile")
    public UserDto getMyProfile() {
        return service.getMyProfile();
    }

    @PostMapping("/requestOwnerRole")
    public void requestOwnerRole(
            @RequestParam("businessNumber")
            String businessNumber
    ){
        service.requestOwnerRole(businessNumber);
    }

    @PostMapping("/requestDriverRole")
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
