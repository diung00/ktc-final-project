package com.example.ChoiGangDeliveryApp.user.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation extends BaseEntity {
    private double latitude;
    private double longitude;

    @OneToOne(mappedBy = "userLocation", cascade = CascadeType.ALL)
    private UserEntity user;
}
