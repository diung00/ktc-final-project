package com.example.ChoiGangDeliveryApp.user.entity;

import com.example.ChoiGangDeliveryApp.common.base.BaseEntity;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String nickname;
    private Integer age;
    private String address;

    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private DriverEntity driver;

    private String profileImgPath;







    private boolean emailVerified;
    private String rejectionReason;


    @OneToOne(mappedBy = "owner")
    private RestaurantsEntity restaurant;


}
