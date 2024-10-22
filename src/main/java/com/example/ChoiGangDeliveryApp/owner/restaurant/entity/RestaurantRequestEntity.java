package com.example.ChoiGangDeliveryApp.owner.restaurant.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.RequestType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantRequestEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantsEntity restaurant;

    @Enumerated(EnumType.STRING)
    private RequestType requestType; // OPEN or CLOSE

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status; // PENDING, APPROVED, or DECLINED

    private String rejectionReason;

}
