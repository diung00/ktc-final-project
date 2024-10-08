package com.example.ChoiGangDeliveryApp.owner.menu.dto;

import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {

    private Long id;
    private String name;
    private String description;
    private int price;
    private Long restaurantId;
    private String menuImageUrl;

    public static MenuDto fromEntity(MenuEntity entity) {
        return MenuDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .restaurantId(entity.getRestaurant().getId())
                .menuImageUrl(entity.getMenuImageUrl())
                .build();

    }
}
