package com.example.ChoiGangDeliveryApp.owner.menu.repo;

import com.example.ChoiGangDeliveryApp.owner.menu.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
}
