package com.example.ChoiGangDeliveryApp.config;

import com.example.ChoiGangDeliveryApp.jwt.JwtTokenFilter;
import com.example.ChoiGangDeliveryApp.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService service;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                                    "/users/login",
                                    "/users/signup"
                            )
                            .anonymous();
                    auth.requestMatchers(
                                    "/users/details",
                                    "/users/profile",
                                    "/users/get-user-info"
                            )
                            .authenticated();
                    auth.requestMatchers(
                                    "/admin/**"
                            )
                            .hasRole("ADMIN");
                    auth.requestMatchers(
                                    "/shops/my-shop/**"
                            )
                            .hasRole("OWNER");
                    auth.requestMatchers("/error", "/static/**", "/views/**", "/")
                            .permitAll();
                    auth.requestMatchers("/customers/**")
                            .hasRole("CUSTOMER");
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                service
                        ),
                        AuthorizationFilter.class
                );


        return http.build();
    }

}
