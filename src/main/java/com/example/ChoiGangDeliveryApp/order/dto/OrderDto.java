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
    private String deliveryAddress;
    private Long restaurantId;
    private LocalDateTime orderDate;
    private String orderStatus;
    private double totalMenusPrice;
    private double shippingFee;
    private double totalAmount;
    private LocalDateTime estimatedArrivalTime;

    @Size(max = 255)
    private String note;

    public static OrderDto fromEntity(OrderEntity entity) {
        return OrderDto.builder()
                .id(entity.getId())
                .driverId(entity.getDriver() != null ? entity.getDriver().getId() : null)
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .deliveryAddress(entity.getDeliveryAddress())
                .restaurantId(entity.getRestaurant() != null ? entity.getRestaurant().getId() : null)
                .orderStatus(entity.getOrderStatus().name())
                .totalMenusPrice(entity.calculateTotalMenusPrice())
                .shippingFee(entity.getShippingFee())
                .totalAmount(entity.calculateTotalAmount())
                .estimatedArrivalTime(entity.getEstimatedArrivalTime())
                .note(entity.getNote())
                .build();
    }

}
