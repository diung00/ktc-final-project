package com.example.ChoiGangDeliveryApp.owner.menu.dto;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.enums.MenuStatus;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
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
    private Long id; //menu id
    private Long restaurantId;
    private String name; // menu's name
    private double price; //menu's price
    private String description;
    private MenuStatus menuStatus; //AVAILABLE, SOLD_OUT
    private int preparationTime;
    private CuisineType cuisineType;
    private String imagePath;

    public static MenuDto fromEntity(MenuEntity entity) {
        return MenuDto.builder()
                .id(entity.getId())
                .restaurantId(entity.getRestaurant() != null ? entity.getRestaurant().getId() : null)
                .name(entity.getName())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .menuStatus(entity.getMenuStatus())
                .preparationTime(entity.getPreparationTime())
                .cuisineType(entity.getCuisineType())
                .imagePath(entity.getImagePath())
                .build();
    }
    public MenuEntity toEntity(RestaurantsEntity restaurant) {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setId(this.id);
        menuEntity.setName(this.name);
        menuEntity.setPrice(this.price);
        menuEntity.setDescription(this.description);
        menuEntity.setMenuStatus(this.menuStatus);
        menuEntity.setPreparationTime(this.preparationTime);
        menuEntity.setCuisineType(this.cuisineType);
        menuEntity.setImagePath(this.imagePath);
        menuEntity.setRestaurant(restaurant);
        return menuEntity;
    }
}
