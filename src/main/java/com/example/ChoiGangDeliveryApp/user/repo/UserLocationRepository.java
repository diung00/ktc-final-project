package com.example.ChoiGangDeliveryApp.user.repo;

import com.example.ChoiGangDeliveryApp.user.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {
}
