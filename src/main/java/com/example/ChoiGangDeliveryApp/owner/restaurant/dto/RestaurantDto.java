package com.example.ChoiGangDeliveryApp.owner.restaurant.dto;

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

}
