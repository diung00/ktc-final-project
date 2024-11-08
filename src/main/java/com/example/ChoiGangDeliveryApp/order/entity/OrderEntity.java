package com.example.ChoiGangDeliveryApp.order.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
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

// this class use to manage general order information
// order user, driver, restaurant, order status, amount...
public class OrderEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id")
    private DriverEntity driver; // driver who is shipping this order

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    private UserEntity user; // ordered customer

    private String deliveryAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantsEntity restaurant; // restaurant where process this order

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenuEntity> orderMenus = new ArrayList<>();

//    private LocalDateTime orderDate; // CreateAt already has been existed in BaseEntity
    private double totalMenusPrice; // Sum of (menuPrice * quantity)

    @Builder.Default
    private double shippingFee = 3000;
    private double totalAmount; // = totalMenusPrice + shippingFee

    private LocalDateTime estimatedArrivalTime; // preparationTime of menus in order + betalTime
    private String note; //customer note

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //PAID,CANCELLED,COOKING,WAITING_FOR_DRIVER,DRIVER_ACCEPT,ON_DELIVERY, COMPLETED

    // Method to calculate the total price of all menus in the order
    public double calculateTotalMenusPrice() {
        return orderMenus.stream()
                .mapToDouble(OrderMenuEntity::getTotalPrice)
                .sum();
    }

    // Method to calculate the total order amount (menu prices + shipping fee)
    public double calculateTotalAmount() {
        return calculateTotalMenusPrice() + shippingFee;
    }

    // Add an OrderMenu to the order and update its reference
    public void addOrderMenu(OrderMenuEntity orderMenu) {
        orderMenus.add(orderMenu);
        orderMenu.setOrder(this);
        updateOrderTotals();
    }

    // Remove an OrderMenu from the order and update the relationship
    public void removeOrderMenu(OrderMenuEntity orderMenu) {
        orderMenus.remove(orderMenu);
        orderMenu.setOrder(null);
        updateOrderTotals();
    }

    // Method to update the total price and total amount after adding or removing an order menu
    public void updateOrderTotals() {
        this.totalMenusPrice = calculateTotalMenusPrice();
        this.totalAmount = calculateTotalAmount();
        setEstimatedArrivalTime();
    }

    // Method to set the estimated arrival time based on the creation time, preparation time, and travel time
    public void setEstimatedArrivalTime() {
        int defaultPreparationTime = 15;
        int defaultTravelTime = 20;
        int totalPreparationTime = orderMenus.stream()
                .mapToInt(orderMenu ->
                {
                    Integer preparationTime = orderMenu.getMenu().getPreparationTime();
                    return (preparationTime != null) ? preparationTime : defaultPreparationTime;
                })
                .sum();
        LocalDateTime orderCreatedAt = this.getCreatedAt() != null ? this.getCreatedAt() : LocalDateTime.now();

        this.estimatedArrivalTime = orderCreatedAt
                .plusMinutes(totalPreparationTime)
                .plusMinutes(defaultTravelTime);
    }

    private boolean isAssigned;

}
