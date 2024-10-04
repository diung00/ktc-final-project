package com.example.ChoiGangDeliveryApp.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverRegDto {
    @Pattern(regexp = "^[a-zA-Z가-힣]{3,12}$", message = "닉네임은 영어 또는 한국어로 3글자 이상, 12글자 이하여야 합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()-_+=]{8,15}$",
            message = "비밀번호는 영어와 숫자가 포함된 8자리 이상, 15자리 이하의 문자열이어야 합니다.")
    private String password;
    private String confirmPassword;

    @Email(message = "유효한 이메일을 입력하세요.")
    private String email;
    private String phone;
    private String address;
    private String licenseNumber;

}
