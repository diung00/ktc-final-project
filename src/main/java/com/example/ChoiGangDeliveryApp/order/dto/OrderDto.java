package com.example.ChoiGangDeliveryApp.order.dto;


import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.user.entity.UserLocation;
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
    private String username;
    private Double userLatitude;
    private Double userLongitude;
    private String deliveryAddress;
    private String restaurantAddress;
    private Long restaurantId;
    private String restaurantName;
    private Double restaurantLatitude;
    private Double restaurantLongitude;
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
                .username(entity.getUser().getUsername())
                .userLatitude(entity.getUser().getUserLocation().getLatitude())
                .userLongitude(entity.getUser().getUserLocation().getLongitude())
                .deliveryAddress(entity.getDeliveryAddress())
                .restaurantAddress(entity.getRestaurant() != null ? entity.getRestaurant().getAddress() : null)
                .restaurantId(entity.getRestaurant() != null ? entity.getRestaurant().getId() : null)
                .restaurantLatitude(entity.getRestaurant().getLatitude())
                .restaurantLongitude(entity.getRestaurant().getLongitude())
                .restaurantName(entity.getRestaurant().getName())
                .orderStatus(entity.getOrderStatus().name())
                .totalMenusPrice(entity.getTotalMenusPrice())
                .shippingFee(entity.getShippingFee())
                .totalAmount(entity.getTotalAmount())
                .estimatedArrivalTime(entity.getEstimatedArrivalTime())
                .note(entity.getNote())
                .build();
    }

}
