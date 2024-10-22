package com.example.ChoiGangDeliveryApp.user.repo;

import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.user.entity.OwnerRoleRequest;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRoleRequestRepository extends JpaRepository<OwnerRoleRequest, Long> {
    Optional<OwnerRoleRequest> findByUserAndStatus(UserEntity currentUser, ApprovalStatus approvalStatus);
    Optional<OwnerRoleRequest> findByUser(UserEntity user);
}
