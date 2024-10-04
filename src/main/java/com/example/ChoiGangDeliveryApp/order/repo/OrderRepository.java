package com.example.ChoiGangDeliveryApp.order.repo;

import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
