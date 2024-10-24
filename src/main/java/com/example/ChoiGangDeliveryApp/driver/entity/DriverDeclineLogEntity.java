package com.example.ChoiGangDeliveryApp.driver.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DriverDeclineLogEntity extends BaseEntity {
    @Column(nullable = false)
    private Long driverId;
    @Column(nullable = false)
    private Long orderId;
    private String reason;

}
