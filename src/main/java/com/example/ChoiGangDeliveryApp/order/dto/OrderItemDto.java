//package com.example.ChoiGangDeliveryApp.order.dto;
//
//import com.example.ChoiGangDeliveryApp.order.entity.MenuOrderEntity;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class OrderItemDto {
//    private Long id;
//    private Long itemId;
//    private int quantity;
//    private int itemPrice;
//
//    public static OrderItemDto fromEntity(MenuOrderEntity menuOrderEntity) {
//        return OrderItemDto.builder()
//                .id(menuOrderEntity.getId())
//                .itemId(menuOrderEntity.getOrder().getId())
//                .quantity(menuOrderEntity.getQuantity())
//                .itemPrice(menu)
//                .build();
//    }
//}
