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

    public static OrderDto fromEntity(OrderEntity orderEntity) {
        return OrderDto.builder()
                .id(orderEntity.getId())
                .driverId(orderEntity.getDriver() != null ? orderEntity.getDriver().getId() : null)
                .userId(orderEntity.getUser() != null ? orderEntity.getUser().getId() : null)
                .restaurantId(orderEntity.getRestaurant() != null ? orderEntity.getRestaurant().getId() : null)
                .orderDate(orderEntity.getOrderDate())
                .orderStatus(orderEntity.getOrderStatus().toString())
                .totalMenuPrice(orderEntity.getTotalMenusPrice())
                .shippingFee(orderEntity.getShippingFee())
                .totalAmount(orderEntity.getTotalAmount())
                .estimatedArrivalTime(orderEntity.getEstimatedArrivalTime())
                .note(orderEntity.getNote())
                .build();
    }
}
