package com.example.ChoiGangDeliveryApp.driver.repo;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverDeclineLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DriverDeclineLogRepository extends JpaRepository<DriverDeclineLogEntity, Long> {


    int countDeclinesByDriverIdAndCreatedAtAfter(Long driverId, LocalDateTime startOfDay);
}
