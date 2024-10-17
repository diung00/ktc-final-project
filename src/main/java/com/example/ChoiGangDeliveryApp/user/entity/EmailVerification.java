package com.example.ChoiGangDeliveryApp.user.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_verification")
public class EmailVerification extends BaseEntity {
    private String email;
    private String verifyCode;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status; //SENT, VERIFIED


}
