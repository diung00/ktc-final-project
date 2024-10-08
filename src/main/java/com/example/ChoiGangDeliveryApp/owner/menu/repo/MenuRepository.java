package com.example.ChoiGangDeliveryApp.owner.menu.repo;

import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    List<MenuEntity> findByRestaurantId(Long restaurantId);

    @Query("SELECT m FROM MenuEntity m JOIN m.restaurant r WHERE " +
            "LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:userLng)) + " +
            "sin(radians(:userLat)) * sin(radians(r.latitude)))) <= 5")
    List<MenuEntity> findByNameContainingAndNearby(@Param("name") String name,
                                                   @Param("userLat") double userLat,
                                                   @Param("userLng") double userLng);


}
