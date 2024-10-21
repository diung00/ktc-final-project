package com.example.ChoiGangDeliveryApp.user.repo;

import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String admin);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserEntity> findUserByEmail(String email);

    Optional<UserEntity> findUserById(Long userId);

    Optional<UserEntity> findByBusinessNumber(String businessNumber);

    Optional<UserEntity> findByLicenseNumber(String licenseNumber);

    List<UserEntity> findByRole(UserRole role);
}
