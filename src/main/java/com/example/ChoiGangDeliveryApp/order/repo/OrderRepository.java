package com.example.ChoiGangDeliveryApp.order.repo;

import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByDriverIdAndOrderStatusIn(Long driverId, List<OrderStatus> statuses);

    Optional<OrderEntity> findOrderById(Long id);

    List<OrderEntity> findAllByRestaurant(RestaurantsEntity restaurant);

    List<OrderEntity> findByOrderStatus(OrderStatus orderStatus);

    List<OrderEntity> findAllByDriverId(Long driverId);

    List<OrderEntity> findAllByUser(UserEntity currentUser);
}
