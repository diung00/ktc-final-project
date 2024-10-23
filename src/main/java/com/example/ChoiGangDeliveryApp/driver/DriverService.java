package com.example.ChoiGangDeliveryApp.driver;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.DriverStatus;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.order.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;


    // UNAVAILABLE MODE
    public void setDriverUnavailable(Long driverId) {
        DriverEntity driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setDriverStatus(DriverStatus.UNAVAILABLE);
        driverRepository.save(driver);
    }

    // AVAILABLE MODE
    public void setDriverAvailable(Long driverId) {
        DriverEntity driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setDriverStatus(DriverStatus.AVAILABLE);
        driverRepository.save(driver);
    }

    // VIEW ORDER(CANCELLED, COMPLETED)
    public List<OrderDto> getCompletedAndCancelledOrders(Long driverId) {
        List<OrderStatus> statuses = Arrays.asList(OrderStatus.COMPLETED, OrderStatus.CANCELLED);

        List<OrderEntity> orders = orderRepository.findByDriverIdAndOrderStatusIn(driverId, statuses);

        return orders.stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }
}
