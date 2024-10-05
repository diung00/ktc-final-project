package com.example.ChoiGangDeliveryApp.user.oAuth2;

import com.example.ChoiGangDeliveryApp.jwt.JwtTokenUtils;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import com.example.ChoiGangDeliveryApp.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtTokenUtils tokenUtils;


    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String provider = oAuth2User.getAttribute("provider");
        String providerId = oAuth2User.getAttribute("id").toString();

        String email = oAuth2User.getAttribute("email");
        String username = String.format("{%s}%s", provider, email);

            if (!userService.userExists(username)) {
                userService.createUserByOAuth2(username, providerId, providerId);
            }

        UserDetails userDetails = (UserDetails) userService.loadUserByUsername(username);
        String jwt = tokenUtils.generateToken(userDetails);
        String targetUrl = String.format("http://localhost:8080/users/validate?token=%s", jwt);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}
