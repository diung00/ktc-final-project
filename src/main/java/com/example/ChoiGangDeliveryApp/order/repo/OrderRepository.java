package com.example.ChoiGangDeliveryApp.order.repo;

import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUser(UserEntity user);

}
