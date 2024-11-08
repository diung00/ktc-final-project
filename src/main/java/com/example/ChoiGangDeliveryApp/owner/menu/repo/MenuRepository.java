package com.example.ChoiGangDeliveryApp.owner.menu.repo;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    List<MenuEntity> findByNameContaining(String name);

    List<MenuEntity> findByCuisineType(CuisineType cuisineType);

    List<MenuEntity> findByRestaurantId(Long restaurantId);
    List<MenuEntity> findByRestaurantIn(List<RestaurantsEntity> restaurants);


    Optional<MenuEntity> findByName(String menuName);
}
