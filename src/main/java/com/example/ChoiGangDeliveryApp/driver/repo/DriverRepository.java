package com.example.ChoiGangDeliveryApp.driver.repo;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverOrderEntity;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
    Optional<DriverEntity> findByUser(UserEntity currentUser);
//    List<DriverOrderEntity> findByDriverId(Long driverId);
//
//    List<OrderEntity> findByDriverIdAndOrderStatusIn(Long driverId, List<OrderStatus> statuses);

}
