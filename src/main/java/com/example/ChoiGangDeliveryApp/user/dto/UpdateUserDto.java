package com.example.ChoiGangDeliveryApp.user.dto;

import com.example.ChoiGangDeliveryApp.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserDto {
    private String name;
    private String nickname;
    private Integer age;
    private String phone;
    private String address;
}
