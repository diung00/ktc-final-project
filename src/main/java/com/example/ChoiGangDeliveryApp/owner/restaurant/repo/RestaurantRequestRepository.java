package com.example.ChoiGangDeliveryApp.owner.restaurant.repo;

import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantRequestEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRequestRepository extends JpaRepository<RestaurantRequestEntity, Long> {
    List<RestaurantRequestEntity> findByStatus(ApprovalStatus approvalStatus);
    Optional<RestaurantRequestEntity> findById (Long id);

    List<RestaurantRequestEntity> findByRestaurant(RestaurantsEntity restaurant);
}
