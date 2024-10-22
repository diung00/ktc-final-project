package com.example.ChoiGangDeliveryApp.owner.restaurant.dto;

import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.RequestType;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantRequestEntity;
import lombok.Data;

@Data
public class RestaurantRequestDto {
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private RequestType requestType; // (OPEN, CLOSE)
    private ApprovalStatus status; // (PENDING, APPROVED, DECLINED)
    private String rejectionReason;

    public static RestaurantRequestDto fromEntity(RestaurantRequestEntity entity) {
        RestaurantRequestDto dto = new RestaurantRequestDto();
        dto.setId(entity.getId());
        dto.setRestaurantId(entity.getRestaurant().getId());
        dto.setRestaurantName(entity.getRestaurant().getName());
        dto.setRequestType(entity.getRequestType());
        dto.setStatus(entity.getStatus());
        dto.setRejectionReason(entity.getRejectionReason());
        return dto;
    }
}
