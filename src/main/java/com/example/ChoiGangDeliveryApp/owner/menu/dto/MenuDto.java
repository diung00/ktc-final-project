package com.example.ChoiGangDeliveryApp.owner.menu.dto;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private CuisineType cuisineType;
    private Long categoryId;
    private String image;
    private LocalDateTime customEstimatedPreparationTime;

    public static MenuDto fromEntity(MenuEntity entity) {
        return MenuDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .cuisineType(entity.getRestaurant().getCuisineType())
                .categoryId(entity.getCategory().getId())
//                .image(entity.getImage)
                .customEstimatedPreparationTime(entity.getPreparationTime())
                .build();
    }
}
