package com.example.ChoiGangDeliveryApp.config;

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
    private final PasswordEncoder passwordEncoder;
    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                UserEntity admin = UserEntity.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("12345"))
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
