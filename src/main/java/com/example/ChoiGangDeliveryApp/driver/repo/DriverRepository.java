package com.example.ChoiGangDeliveryApp.driver.repo;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverOrderEntity;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
//    List<DriverOrderEntity> findByDriverId(Long driverId);
//
//    List<OrderEntity> findByDriverIdAndOrderStatusIn(Long driverId, List<OrderStatus> statuses);

}
