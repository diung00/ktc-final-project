package com.example.ChoiGangDeliveryApp.owner.menu.repo;

import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
}
