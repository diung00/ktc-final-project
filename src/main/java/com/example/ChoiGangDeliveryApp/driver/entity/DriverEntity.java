package com.example.ChoiGangDeliveryApp.driver.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.enums.DriverStatus;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
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
public class DriverEntity extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder.Default
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<DriverOrderEntity> orders = new ArrayList<>(); //List of orders that driver process

    private double latitude;
    private double longitude;

    @Enumerated(EnumType.STRING)
    private DriverStatus driverStatus;

    public int getOrdersCount() {
        return orders.size();
    }

}
