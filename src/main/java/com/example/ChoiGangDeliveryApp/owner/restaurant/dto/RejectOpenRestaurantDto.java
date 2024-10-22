package com.example.ChoiGangDeliveryApp.owner.restaurant.dto;

import lombok.Getter;

@Getter
public class RejectOpenRestaurantDto {
    private Long requestId;
    private String rejectReason;
}
