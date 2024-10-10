package com.example.ChoiGangDeliveryApp.owner.restaurant.repo;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantsEntity, Long> {

    //tìm cử hàng có bán kính cách vị trí của user 5km
    @Query("SELECT r FROM RestaurantsEntity r WHERE " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:userLng)) + sin(radians(:userLat)) * sin(radians(r.latitude)))) <= 5")
    List<RestaurantsEntity> findNearbyRestaurants(@Param("userLat") double userLat, @Param("userLng") double userLng);


    //tìm cử hàng có tên chứa từ khóa và bán kính cách vị trí của user 5km
    @Query("SELECT r FROM RestaurantsEntity r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:userLng)) + sin(radians(:userLat)) * sin(radians(r.latitude)))) <= 5")
    List<RestaurantsEntity> findByNameContainingAndWithin5km(@Param("name") String name,
                                                             @Param("userLat") double userLat,
                                                             @Param("userLng") double userLng);


    Optional<RestaurantsEntity> findById(Long id);


    @Query("SELECT r FROM RestaurantsEntity r WHERE " +
            "r.cuisineType = :cuisineType AND " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:userLng)) + " +
            "sin(radians(:userLat)) * sin(radians(r.latitude)))) <= 5")
    List<RestaurantsEntity> findByCuisineTypeAndNearby(@Param("cuisineType") CuisineType cuisineType,
                                                       @Param("userLat") double userLat,
                                                       @Param("userLng") double userLng);


}
