package com.example.ChoiGangDeliveryApp.user.dto;

import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String nickname;
    private Integer age;
    private String phone;
    private String email;
    private UserRole role;
    private String address;
    private String profileImgPath;
    private boolean emailVerified;
    private String licenseNumber;
    private String businessNumber;

    public static UserDto fromEntity(UserEntity entity){
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .name(entity.getName())
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .role(entity.getRole())
                .address(entity.getAddress())
                .profileImgPath(entity.getProfileImgPath())
                .emailVerified(entity.isEmailVerified())
                .licenseNumber(entity.getLicenseNumber())
                .businessNumber(entity.getBusinessNumber())
                .build();
    }
}
