package com.example.ChoiGangDeliveryApp.user.dto;

import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.user.entity.OwnerRoleRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerRoleRequestDto {
    private String businessNumber;
    private UserRole userRole;
    private ApprovalStatus status;
    private String rejectionReason;

    public static OwnerRoleRequestDto fromEntity(OwnerRoleRequest request) {
        return OwnerRoleRequestDto.builder()
                .businessNumber(request.getBusinessNumber())
                .userRole(request.getUser().getRole())
                .status(request.getStatus())
                .rejectionReason(request.getRejectionReason())
                .build();
    }
}
