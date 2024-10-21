package com.example.ChoiGangDeliveryApp.user.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverRoleRequest extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    private String rejectionReason;

}
