package com.example.ChoiGangDeliveryApp.owner.restaurant.repo;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.security.config.CustomUserDetails;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantsEntity, Long> {
    Optional<RestaurantsEntity> findByOwner(UserEntity owner);

    List<RestaurantsEntity> findRestaurantsByCuisineType(CuisineType cuisineType);
}
