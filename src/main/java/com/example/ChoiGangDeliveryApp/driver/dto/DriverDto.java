package com.example.ChoiGangDeliveryApp.driver.dto;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
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
    private List<Long> orderIds;
    private double latitude;
    private double longitude;
    private String driverStatus;

    public static DriverDto toDto(DriverEntity driverEntity) {
        return DriverDto.builder()
                .id(driverEntity.getId())
                .orderIds(driverEntity.getOrders().stream()
                        .map(OrderEntity::getId)
                        .collect(Collectors.toList()))
                .latitude(driverEntity.getLatitude())
                .longitude(driverEntity.getLongitude())
                .driverStatus(driverEntity.getDriverStatus().name())
                .build();
    }
    public static DriverEntity toEntity(DriverDto driverDto, UserEntity user) {
        DriverEntity driverEntity = new DriverEntity();
        driverEntity.setLatitude(driverDto.getLatitude());
        driverEntity.setLongitude(driverDto.getLongitude());
        driverEntity.setDriverStatus(DriverStatus.valueOf(driverDto.getDriverStatus()));
        return driverEntity;
    }



}
