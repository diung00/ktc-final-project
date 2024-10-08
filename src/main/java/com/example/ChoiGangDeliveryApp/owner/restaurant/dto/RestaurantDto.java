package com.example.ChoiGangDeliveryApp.owner.restaurant.dto;

import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String openingHours;
    private String cuisineType;
    private double rating;
    private String restImage;
    private String description;
    private String approvalStatus;
    private Long ownerId;
    private String ownerName;


    public static RestaurantDto fromEntity(RestaurantEntity entity) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .openingHours(entity.getOpeningHours())
                .cuisineType(entity.getCuisineType().name())
                .rating(entity.getRating())
                .restImage(entity.getRestImage())
                .description(entity.getDescription())
                .ownerName(entity.getOwner().getName())
                .build();
    }

}
