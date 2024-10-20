package com.example.ChoiGangDeliveryApp.order.dto;


import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderDto {
    private Long id;
    private Long driverId;
    private Long userId;
    private Long restaurantId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private double totalMenuPrice;
    private double shippingFee;
    private double totalAmount;
    private LocalDateTime estimatedArrivalTime;

    @Size(max = 255)
    private String note;

    public static OrderDto fromEntity(OrderEntity entity) {
        return OrderDto.builder()
                .id(entity.getId())
                .driverId(entity.getDriver().getId())
                .userId(entity.getUser().getId())
                .restaurantId(entity.getRestaurant().getId())
                .orderDate(entity.getOrderDate())
                .orderStatus(String.valueOf(entity.getOrderStatus()))
                .totalMenuPrice(entity.getTotalMenusOrderPrice())
                .shippingFee(entity.getShippingFee())
                .totalAmount(entity.getTotalAmount())
                .estimatedArrivalTime(entity.getEstimatedArrivalTime())
                .note(entity.getNote())
                .build();
    }

}
