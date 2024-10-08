package com.example.ChoiGangDeliveryApp.owner.restaurant.repo;

import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRequestRepository extends JpaRepository<RestaurantRequestEntity, Long> {
}
