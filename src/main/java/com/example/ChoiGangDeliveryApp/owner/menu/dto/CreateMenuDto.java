package com.example.ChoiGangDeliveryApp.owner.menu.dto;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.enums.MenuStatus;
import lombok.Data;

@Data
public class CreateMenuDto {
    private String name; // menu's name
    private int price; //menu's price
    private String description;
    private MenuStatus menuStatus; //AVAILABLE, SOLD_OUT
    private int preparationTime;
    private CuisineType cuisineType;
}
