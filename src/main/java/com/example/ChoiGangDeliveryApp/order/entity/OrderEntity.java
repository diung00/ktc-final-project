package com.example.ChoiGangDeliveryApp.order.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantEntity;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id")
    private DriverEntity driver; // driver who is shipping this order

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user; // ordered customer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @OneToMany(mappedBy = "order")
    private List<MenuOrderEntity> menuOrders;


    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private double totalMenusOrderPrice;
    private double shippingFee;
    private double totalAmount;
    private LocalDateTime estimatedArrivalTime;
    private String note;

}
