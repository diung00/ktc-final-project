package com.example.ChoiGangDeliveryApp.order.repo;

import com.example.ChoiGangDeliveryApp.order.entity.OrderMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMenuRepository extends JpaRepository<OrderMenuEntity, Long> {
}
