package com.example.ChoiGangDeliveryApp.order.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Order_Menu")
//OrderItem: Manage specific menu in an order.
public class OrderMenuEntity extends BaseEntity {

    //order Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    //menu id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;

    //restaurant id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantsEntity restaurant;

    // calculate amount
    private int quantity;
    private double menuPrice;
    private double totalPrice;

    // Update totalPrice whenever quantity or menuPrice changes
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotalPrice();
    }

    public void setMenuPrice(double menuPrice) {
        this.menuPrice = menuPrice;
        updateTotalPrice();
    }

    // Method to calculate and set totalPrice
    private void updateTotalPrice() {
        this.totalPrice = menuPrice * quantity;
    }

    public static OrderMenuEntity createOrderMenu(MenuEntity menu, int quantity) {
        OrderMenuEntity orderMenu = new OrderMenuEntity();
        orderMenu.setMenu(menu);
        orderMenu.setMenuPrice(menu.getPrice());
        orderMenu.setQuantity(quantity);
        orderMenu.updateTotalPrice();  // Set totalPrice at creation time
        return orderMenu;
    }
}
