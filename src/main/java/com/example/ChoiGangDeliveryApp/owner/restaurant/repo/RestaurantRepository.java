package com.example.ChoiGangDeliveryApp.owner.restaurant.repo;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    //tìm cử hàng có bán kính cách vị trí của user 5km
    @Query("SELECT r FROM RestaurantEntity r WHERE " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:userLng)) + sin(radians(:userLat)) * sin(radians(r.latitude)))) <= 5")
    List<RestaurantEntity> findNearbyRestaurants(@Param("userLat") double userLat, @Param("userLng") double userLng);


    //tìm cử hàng có tên chứa từ khóa và bán kính cách vị trí của user 5km
    @Query("SELECT r FROM RestaurantEntity r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%')) AND " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:userLng)) + sin(radians(:userLat)) * sin(radians(r.latitude)))) <= 5")
    List<RestaurantEntity> findByNameContainingAndWithin5km(@Param("name") String name,
                                                            @Param("userLat") double userLat,
                                                            @Param("userLng") double userLng);


    Optional<RestaurantEntity> findById(Long id);


    @Query("SELECT r FROM RestaurantEntity r WHERE " +
            "r.cuisineType = :cuisineType AND " +
            "(6371 * acos(cos(radians(:userLat)) * cos(radians(r.latitude)) * cos(radians(r.longitude) - radians(:userLng)) + " +
            "sin(radians(:userLat)) * sin(radians(r.latitude)))) <= 5")
    List<RestaurantEntity> findByCuisineTypeAndNearby(@Param("cuisineType") CuisineType cuisineType,
                                                      @Param("userLat") double userLat,
                                                      @Param("userLng") double userLng);


}
