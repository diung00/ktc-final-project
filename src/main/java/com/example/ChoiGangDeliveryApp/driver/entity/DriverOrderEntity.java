package com.example.ChoiGangDeliveryApp.driver.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "driver-orders")
public class DriverOrderEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private DriverEntity driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    // Delivery time is set based on the estimated arrival time from OrderEntity
    private LocalDateTime arrivalTime;

    // Constructor or method to set delivery time from OrderEntity
    public DriverOrderEntity(DriverEntity driver, OrderEntity order) {
        this.driver = driver;
        this.order = order;
        this.arrivalTime = order.getEstimatedArrivalTime();
    }




}
