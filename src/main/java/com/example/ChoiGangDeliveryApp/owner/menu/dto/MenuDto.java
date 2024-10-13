package com.example.ChoiGangDeliveryApp.owner.menu.dto;

import com.example.ChoiGangDeliveryApp.enums.MenuStatus;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public class MenuDto {
    private String name;
    private int price;
    private String description;
    private MenuStatus menuStatus;

    public static MenuDto fromEntity(MenuEntity entity){
        return MenuDto.builder()
               .name(entity.getName())
               .price(entity.getPrice())
               .description(entity.getDescription())
               .menuStatus(entity.getMenuStatus())
               .build();
    }
}
