package com.example.ChoiGangDeliveryApp.driver.dto;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverOrderEntity;
import com.example.ChoiGangDeliveryApp.enums.DriverStatus;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {
    private Long id;
    private Long userId;
    private List<DriverOrderDto> orders; //list DriverOrders
    private double latitude;
    private double longitude;
    private String driverStatus;

    // Convert from DriverEntity into DriverDto
    public static DriverDto fromEntity(DriverEntity driverEntity) {
        return DriverDto.builder()
                .id(driverEntity.getId())
                .userId(driverEntity.getUser().getId())
                .orders(driverEntity.getOrders().stream()
                        .map(DriverOrderDto::fromEntity)
                        .collect(Collectors.toList()))
                .latitude(driverEntity.getLatitude())
                .longitude(driverEntity.getLongitude())
                .driverStatus(driverEntity.getDriverStatus().name())
                .build();
    }
    public static DriverEntity toEntity(DriverDto driverDto, UserEntity user, List<OrderEntity> orders) {
        DriverEntity driverEntity = DriverEntity.builder()
                .user(user)
                .latitude(driverDto.getLatitude())
                .longitude(driverDto.getLongitude())
                .driverStatus(DriverStatus.valueOf(driverDto.getDriverStatus()))
                .build();

        if (driverDto.getOrders() != null) {
            List<DriverOrderEntity> driverOrders = driverDto.getOrders().stream()
                    .map(orderDto -> {
                        OrderEntity orderEntity = orders.stream()
                                .filter(o -> o.getId().equals(orderDto.getOrderId()))
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderDto.getOrderId()));
                        return DriverOrderDto.toEntity(orderDto, driverEntity, orderEntity);
                    })
                    .collect(Collectors.toList());
            driverEntity.setOrders(driverOrders);
        }
        return driverEntity;
    }



}
