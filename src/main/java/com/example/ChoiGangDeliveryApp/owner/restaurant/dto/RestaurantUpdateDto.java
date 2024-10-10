package com.example.ChoiGangDeliveryApp.owner.restaurant.dto;

import lombok.Data;

@Data
public class RestaurantUpdateDto {
    private String name;
    private String address;
    private String phone;
    private String openingHours;
    private String cuisineType;
    private String description;
}
