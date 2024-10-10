package com.example.ChoiGangDeliveryApp.owner.restaurant.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
public class RestaurantsEntity extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity owner;
    // Restaurant information
    private String name; //restaurant name
    private String address; //restaurant address
    private String phone;
    private String openingHours;
    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;
    private double rating;
    private String RestImage;
    private String description;
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    //Restaurant location
    private double latitude;
    private double longitude;

    //Menu
    @Builder.Default
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private final List<MenuEntity> menus = new ArrayList<>();

    //Order
    @Builder.Default
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
    private final List<OrderEntity> orders = new ArrayList<>();


}
