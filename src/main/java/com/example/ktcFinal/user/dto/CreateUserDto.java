package com.example.ktcFinal.user.dto;

import com.example.ktcFinal.eNum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    private String username;
    private String password;
    private String passCheck;
    private String phone;
    private String email;
    private String role;

    public CreateUserDto(
            String username,
            String password,
            String passCheck,
            String phone,
            String email,
            UserRole role
    ){
        this.username = username;
        this.password = password;
        this.passCheck = passCheck;
        this.phone = phone;
        this.email = email;
        this.role = role.name();
    }
}
