package com.example.ChoiGangDeliveryApp.order.dto;

import com.example.ChoiGangDeliveryApp.order.entity.OrderMenuEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuDto {
    private Long id;
    private Long orderId;
    private Long menuId;
    private String menuName;
    private int quantity;
    private double menuPrice;
    private double totalPrice;

    private Long restaurantId;
    private String restaurantName;


    public static OrderMenuDto fromEntity(OrderMenuEntity orderMenuEntity) {
        return OrderMenuDto.builder()
                .id(orderMenuEntity.getId())
                .orderId(orderMenuEntity.getOrder() != null ? orderMenuEntity.getOrder().getId() : null)
                .menuId(orderMenuEntity.getMenu().getId())
                .menuName(orderMenuEntity.getMenu().getName())
                .restaurantId(orderMenuEntity.getRestaurant() != null ? orderMenuEntity.getRestaurant().getId() : null)
                .restaurantName(orderMenuEntity.getRestaurant().getName())
                .quantity(orderMenuEntity.getQuantity())
                .menuPrice(orderMenuEntity.getMenuPrice())
                .totalPrice(orderMenuEntity.getTotalPrice())
                .build();
    }


}
