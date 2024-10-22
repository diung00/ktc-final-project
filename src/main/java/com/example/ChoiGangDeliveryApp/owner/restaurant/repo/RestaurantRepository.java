package com.example.ChoiGangDeliveryApp.owner.restaurant.repo;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.security.config.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<RestaurantsEntity, Long> {

    @Query("SELECT r FROM RestaurantsEntity r WHERE " +
            "6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(r.latitude)) * " +
            "COS(RADIANS(r.longitude) - RADIANS(:longitude)) + " +
            "SIN(RADIANS(:latitude)) * SIN(RADIANS(r.latitude))) < :distance")
    List<RestaurantsEntity> findRestaurantsWithinRadius(@Param("latitude") double latitude,
                                                        @Param("longitude") double longitude,
                                                        @Param("distance") double distance);


    @Query("SELECT r FROM RestaurantsEntity r WHERE " +
            "r.cuisineType = :cuisineType AND " +
            "6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(r.latitude)) * " +
            "COS(RADIANS(r.longitude) - RADIANS(:longitude)) + " +
            "SIN(RADIANS(:latitude)) * SIN(RADIANS(r.latitude))) < :distance")
    List<RestaurantsEntity> findRestaurantsWithinRadiusByCuisineType(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distance,
            @Param("cuisineType") CuisineType cuisineType);

    @Query("SELECT r FROM RestaurantsEntity r JOIN r.menus m WHERE " +
            "6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(r.latitude)) * " +
            "COS(RADIANS(r.longitude) - RADIANS(:longitude)) + " +
            "SIN(RADIANS(:latitude)) * SIN(RADIANS(r.latitude))) < :distance " +
            "AND m.name LIKE %:menuName%")
    List<RestaurantsEntity> findRestaurantsWithinRadiusByMenuNameContaining(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distance") double distance,
            @Param("menuName") String menuName);

}
