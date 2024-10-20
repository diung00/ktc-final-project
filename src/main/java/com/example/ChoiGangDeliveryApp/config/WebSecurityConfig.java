package com.example.ChoiGangDeliveryApp.config;

import com.example.ChoiGangDeliveryApp.jwt.JwtTokenFilter;
import com.example.ChoiGangDeliveryApp.jwt.JwtTokenUtils;
import com.example.ChoiGangDeliveryApp.user.oAuth2.OAuth2SuccessHandler;
import com.example.ChoiGangDeliveryApp.user.oAuth2.OAuth2UserService;
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
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                                    "/users/login",
                                    "/users/signup",
                            "/users/signup/send-code",
                            "/users/signup/verify",
                            "/users/request-password-reset",
                            "/users/reset-password"
                            )
                            .anonymous();
                    auth.requestMatchers(
                                    "/users/get-my-profile",
                                    "/users/profile",
                                    "/users/get-user-info",
                            "/users/change-password",
                            "/users/upload-profile-image",
                            "/users/request-owner-role",
                            "/users/request-driver-role"
                            )
                            .authenticated();
                    auth.requestMatchers("/restaurants/**").authenticated();
                    auth.requestMatchers(
                                    "/admin/**"
                            )
                            .hasRole("ADMIN");
                    auth.requestMatchers(
                                    "/owners/**", "/restaurants/**", "/menus/**"
                            )
                            .hasRole("OWNER");
                    auth.requestMatchers("/error", "/static/**", "/", "/oauth2/**")
                            .permitAll();

                    auth.requestMatchers( "/customers/**")
                            .hasRole("USER");
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/users/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                        .permitAll()
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
