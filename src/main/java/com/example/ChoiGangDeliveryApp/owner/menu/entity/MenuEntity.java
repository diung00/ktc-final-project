package com.example.ChoiGangDeliveryApp.owner.menu.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.enums.MenuStatus;
import com.example.ChoiGangDeliveryApp.order.entity.MenuOrderEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "menus")
public class MenuEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private RestaurantsEntity restaurant;

    // Menu information
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int price;

    private String description;

    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    private int preparationTime; // time to prepare menu (minute)

    @Builder.Default
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuOrderEntity> menuOrders = new ArrayList<>();

}
