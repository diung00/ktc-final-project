package com.example.ChoiGangDeliveryApp.owner.restaurant.repo;

import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<RestaurantsEntity, Long> {


    // tìm nhà hàng bằng tọa độ và bán kính 3km
    @Query("SELECT r FROM RestaurantEntity r WHERE " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(r.latitude)) * " +
            "cos(radians(r.longitude) - radians(:lng)) + sin(radians(:lat)) * sin(radians(r.latitude)))) <= :radius")
    List<RestaurantsEntity> findRestaurantsWithinRadius(@Param("lat") double latitude,
                                                        @Param("lng") double longitude,
                                                        @Param("radius") double radiusInKm);


}
