package com.example.ChoiGangDeliveryApp.user.controller;

import com.example.ChoiGangDeliveryApp.jwt.JwtTokenUtils;
import com.example.ChoiGangDeliveryApp.jwt.dto.JwtRequestDto;
import com.example.ChoiGangDeliveryApp.jwt.dto.JwtResponseDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserCreateDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.service.UserService;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
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
