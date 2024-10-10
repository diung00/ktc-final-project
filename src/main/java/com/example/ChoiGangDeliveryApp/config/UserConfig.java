package com.example.ChoiGangDeliveryApp.config;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.DriverStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserConfig {
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            //create admin user
            if (!userRepository.existsByUsername("admin")) {
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_ADMIN)
                        .emailVerified(true)
                        .build();
                userRepository.save(admin);
            }

            // Create owner user for testing
            if (!userRepository.existsByUsername("ownerTest1")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest1")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .email("ownerTest1@example.com")
                        .emailVerified(true)
                        .approvalStatus(ApprovalStatus.APPROVED)
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            // Create customer user for testing
            if (!userRepository.existsByUsername("customerTest1")) {
                UserEntity customerUser = UserEntity.builder()
                        .username("customerTest1")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_CUSTOMER)
                        .email("customerTest1@example.com")
                        .emailVerified(true)
                        .approvalStatus(ApprovalStatus.APPROVED)
                        .build();
                userRepository.save(customerUser);
            }

            // Create driver user for testing
            if (!userRepository.existsByUsername("driverTest1")) {
                UserEntity driverUser = UserEntity.builder()
                        .username("driverTest1")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_DRIVER)
                        .licenseNumber("11-850101-1234")
                        .email("driverTest1@example.com")
                        .emailVerified(true)
                        .approvalStatus(ApprovalStatus.APPROVED)
                        .build();
                userRepository.save(driverUser);
                // Create DriverEntity object
                DriverEntity driver = new DriverEntity();
                driver.setUser(driverUser);
                driver.setDriverStatus(DriverStatus.AVAILABLE);

                // save driver to DriverEntity database
                driverRepository.save(driver);
            }
        };
    }
}
