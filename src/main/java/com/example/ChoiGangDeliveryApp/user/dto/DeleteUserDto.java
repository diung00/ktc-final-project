
package com.example.ChoiGangDeliveryApp.user.dto;

import com.example.ChoiGangDeliveryApp.enums.UserDeleteReason;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class DeleteUserDto {
    private String password;

    private UserDeleteReason reason;
}
