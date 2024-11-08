package com.example.ChoiGangDeliveryApp.order.dto;

import com.example.ChoiGangDeliveryApp.order.entity.OrderMenuEntity;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    private Long restaurantId;

    private List<OrderMenuDto> orderMenus;
    private String note;

}
