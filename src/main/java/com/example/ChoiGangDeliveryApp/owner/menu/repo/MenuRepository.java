package com.example.ChoiGangDeliveryApp.owner.menu.repo;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.CategoryEntity;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    List<MenuEntity> findByNameContaining(String name);

    List<MenuEntity> findByCuisineType(CuisineType cuisineType);

    List<MenuEntity> findByCategory(CategoryEntity category);
}
