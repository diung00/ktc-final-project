package com.example.ChoiGangDeliveryApp.user.repo;

import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String admin);

    Optional<UserEntity> findByUsername(String username);
}
