package com.example.ChoiGangDeliveryApp.user.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String nickname;
    private Integer age;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    private String address;
    private String profileImgPath;

    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String licenseNumber;
    private String businessNumber;

    @Builder.Default
    private boolean emailVerified = false; //default is false

    @OneToOne(mappedBy = "owner")
    private RestaurantsEntity restaurant;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private OwnerRoleRequest ownerRoleRequest;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private DriverRoleRequest driverRoleRequest;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_location_id")
    private UserLocation userLocation;


}
