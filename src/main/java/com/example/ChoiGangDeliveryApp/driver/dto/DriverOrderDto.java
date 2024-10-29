package com.example.ChoiGangDeliveryApp.driver.dto;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverOrderEntity;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriverOrderDto {
    private Long driverId;
    private Long orderId;
    private LocalDateTime arrivalTime;
    // Convert from DriverOrderEntity to DriverOrderDto using Builder
    public static DriverOrderDto fromEntity(DriverOrderEntity driverOrderEntity) {
        return DriverOrderDto.builder()
                .driverId(driverOrderEntity.getDriver().getId())
                .orderId(driverOrderEntity.getOrder().getId())
                .arrivalTime(driverOrderEntity.getArrivalTime())
                .build();
    }
    // Convert from DriverOrderDto to DriverOrderEntity
    public static DriverOrderEntity toEntity(DriverOrderDto driverOrderDto, DriverEntity driver, OrderEntity order) {
        return DriverOrderEntity.builder()
                .driver(driver)
                .order(order)
                .arrivalTime(driverOrderDto.getArrivalTime())
                .build();
    }
}
