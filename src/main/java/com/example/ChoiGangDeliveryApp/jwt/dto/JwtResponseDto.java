package com.example.ChoiGangDeliveryApp.jwt.dto;

import com.example.ChoiGangDeliveryApp.enums.UserRole;
import lombok.Data;

@Data
public class JwtResponseDto {
    String token;
    private UserRole userRole;
    private String redirectUrl;
}
