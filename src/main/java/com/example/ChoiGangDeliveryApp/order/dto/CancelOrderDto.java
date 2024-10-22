package com.example.ChoiGangDeliveryApp.order.dto;

import lombok.Getter;

@Getter

public class CancelOrderDto {
    private Long orderId;
    private String cancelReason;
}
