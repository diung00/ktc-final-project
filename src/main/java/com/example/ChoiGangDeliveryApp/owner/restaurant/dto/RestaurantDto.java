package com.example.ChoiGangDeliveryApp.owner.restaurant.dto;

import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
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
    //Restaurant location
    private double latitude;
    private double longitude;


    public static RestaurantDto fromEntity(RestaurantsEntity newRestaurant) {
        return RestaurantDto.builder()
                .id(newRestaurant.getId())
                .name(newRestaurant.getName())
                .address(newRestaurant.getAddress())
                .phone(newRestaurant.getPhone())
                .openingHours(newRestaurant.getOpeningHours())
                .cuisineType(newRestaurant.getCuisineType().name())
                .rating(newRestaurant.getRating())
                .restImage(newRestaurant.getRestImage())
                .description(newRestaurant.getDescription())
                .approvalStatus(newRestaurant.getApprovalStatus().name())
                .ownerId(newRestaurant.getOwner() != null ? newRestaurant.getOwner().getId() : null)
                .latitude(newRestaurant.getLatitude())
                .longitude(newRestaurant.getLongitude())
                .build();
    }
}
