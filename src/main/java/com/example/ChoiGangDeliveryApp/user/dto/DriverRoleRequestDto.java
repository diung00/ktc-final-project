package com.example.ChoiGangDeliveryApp.user.dto;

import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.user.entity.DriverRoleRequest;
import com.example.ChoiGangDeliveryApp.user.entity.OwnerRoleRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverRoleRequestDto {
    private String licenseNumber;
    private UserRole userRole;
    private ApprovalStatus status;
    private String rejectionReason;

    public static DriverRoleRequestDto fromEntity(DriverRoleRequest request) {
        return DriverRoleRequestDto.builder()
                .licenseNumber(request.getLicenseNumber())
                .userRole(request.getUser().getRole())
                .status(request.getStatus())
                .rejectionReason(request.getRejectionReason())
                .build();
    }
}
