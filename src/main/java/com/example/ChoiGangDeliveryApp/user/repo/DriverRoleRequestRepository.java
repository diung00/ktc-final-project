package com.example.ChoiGangDeliveryApp.user.repo;

import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.user.entity.DriverRoleRequest;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRoleRequestRepository extends JpaRepository<DriverRoleRequest, Long> {
    Optional<DriverRoleRequest> findByUserAndStatus(UserEntity currentUser, ApprovalStatus approvalStatus);
}
