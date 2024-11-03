package com.example.ChoiGangDeliveryApp.config;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.enums.DriverStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
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

            // owner test
            if (!userRepository.existsByUsername("ownerTest2")) {
                UserEntity owner1 = UserEntity.builder()
                        .username("ownerTest2")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .email("ownerTest2@example.com")
                        .emailVerified(true)
                        .emailVerified(true)
                        .build();
                userRepository.save(owner1);
                RestaurantsEntity restaurantTest = RestaurantsEntity.builder()
                        .owner(owner1) // Liên kết với UserEntity (ownerTest)
                        .name("Test Restaurant")
                        .address("123 Test Street")
                        .phone("123-456-7890")
                        .openingHours("9:00 AM - 9:00 PM")
                        .cuisineType(CuisineType.ASIAN_FOOD)
                        .rating(4.5)
                        .RestImage("restaurant_image.jpg")
                        .description("A test restaurant for Italian cuisine.")
                        .latitude(40.7128) // Đặt tọa độ ví dụ
                        .longitude(-74.0060)
                        .approvalStatus(ApprovalStatus.APPROVED)
                        .build();
            }

            // Create owner user for testing
            if (!userRepository.existsByUsername("ownerTest1")) {
                UserEntity owner = UserEntity.builder()
                            .username("ownerTest1")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("123412372023")
                        .email("ownerTest1@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            if (!userRepository.existsByUsername("ownerTest2")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest2")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("123456782024")
                        .email("ownerTest2@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            if (!userRepository.existsByUsername("ownerTest3")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest3")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("123456782023")
                        .email("ownerTest3@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }

            if (!userRepository.existsByUsername("ownerTest4")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest4")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("123456782012")
                        .email("ownerTest4@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            if (!userRepository.existsByUsername("ownerTest5")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest5")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("123456782124")
                        .email("ownerTest5@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            if (!userRepository.existsByUsername("ownerTest6")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest6")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("123456122024")
                        .email("ownerTest6@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            if (!userRepository.existsByUsername("ownerTest7")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest7")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("123126782024")
                        .email("ownerTest7@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            if (!userRepository.existsByUsername("ownerTest8")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest8")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("123452382024")
                        .email("ownerTest8@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            if (!userRepository.existsByUsername("ownerTest9")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest9")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("128956782024")
                        .email("ownerTest9@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            if (!userRepository.existsByUsername("ownerTest10")) {
                UserEntity owner = UserEntity.builder()
                        .username("ownerTest10")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_OWNER)
                        .businessNumber("193456781224")
                        .email("ownerTest10@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(owner);
            }
            // Create customer user for testing
            if (!userRepository.existsByUsername("userTest1")) {
                UserEntity customerUser = UserEntity.builder()
                        .username("userTest1")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_USER)
                        .email("userTest1@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(customerUser);
            }

            if (!userRepository.existsByUsername("userTest2")) {
                UserEntity customerUser = UserEntity.builder()
                        .username("userTest2")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_USER)
                        .email("userTest2@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(customerUser);
            }
            if (!userRepository.existsByUsername("userTest3")) {
                UserEntity customerUser = UserEntity.builder()
                        .username("userTest3")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_USER)
                        .email("userTest3@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(customerUser);
            }
            if (!userRepository.existsByUsername("userTest4")) {
                UserEntity customerUser = UserEntity.builder()
                        .username("userTest4")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_USER)
                        .email("userTest4@example.com")
                        .emailVerified(true)
                        .build();
                userRepository.save(customerUser);
            }
            if (!userRepository.existsByUsername("userTest5")) {
                UserEntity customerUser = UserEntity.builder()
                        .username("userTest5")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_USER)
                        .email("userTest5@example.com")
                        .emailVerified(true)
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
                        .build();
                userRepository.save(driverUser);
                // Create DriverEntity object
                DriverEntity driver = new DriverEntity();
                driver.setUser(driverUser);
                driver.setDriverStatus(DriverStatus.AVAILABLE);

                // save driver to DriverEntity database
                driverRepository.save(driver);
            }
            if (!userRepository.existsByUsername("driverTest2")) {
                UserEntity driverUser = UserEntity.builder()
                        .username("driverTest2")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ROLE_DRIVER)
                        .licenseNumber("10-880711-1284")
                        .email("driverTest2@example.com")
                        .emailVerified(true)
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
