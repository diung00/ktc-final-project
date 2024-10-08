package com.example.ChoiGangDeliveryApp.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CancelOrderDto {
    private Long orderId;
    private String reason;
}
