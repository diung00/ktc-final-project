package com.example.ChoiGangDeliveryApp.user.service;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.jwt.JwtTokenUtils;
import com.example.ChoiGangDeliveryApp.owner.menu.dto.MenuDto;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import com.example.ChoiGangDeliveryApp.owner.menu.repo.MenuRepository;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;

import com.example.ChoiGangDeliveryApp.user.dto.UpdateAddressDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final UserRepository userRepository;
    private final AuthenticationFacade authFacade;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;


    public UserDto customerUpdateAddress(UpdateAddressDto dto){
        UserEntity userEntity = authFacade.getCurrentUserEntity();

        userEntity.setAddress(dto.getAddress());

        userRepository.save(userEntity);

        return UserDto.fromEntity(userEntity);
    }

    // public List<OrderDto> viewAllOrder(){}

    //  public void deleteOrder(){}

    public List<RestaurantDto> getNearbyRestaurants(
            double userLat,
            double userLng
    ) {
        List<RestaurantEntity> restaurants =
                restaurantRepository.findNearbyRestaurants(userLat, userLng);
        return restaurants.stream()
                .map(RestaurantDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RestaurantDto> findRestaurantByNameContainingAndNearby(
            String name,
            double userLat,
            double userLng
    ){
        List<RestaurantEntity> restaurants =
                restaurantRepository.findByNameContainingAndWithin5km(name, userLat, userLng);
        return restaurants.stream()
                .map(RestaurantDto::fromEntity)
                .collect(Collectors.toList());

    }

    public List<RestaurantDto> findRestaurantByCuisineTypeContainingAndNearby(
            CuisineType cuisineType,
            double userLat,
            double userLng
    ) {
        List<RestaurantEntity> restaurants =
                restaurantRepository.findByCuisineTypeAndNearby(cuisineType, userLat, userLng);
        return restaurants.stream()
                .map(RestaurantDto::fromEntity)
                .collect(Collectors.toList());
    }


    public RestaurantDto viewOneRestaurant(
            Long restaurantId
    ){
        Optional<RestaurantEntity> restaurant =
                restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()){
            return RestaurantDto.fromEntity(restaurant.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    public List<MenuDto> viewAllMenuOfOneRestaurant(
            Long restaurantId
    ){

        Optional<RestaurantEntity> restaurant =
                restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()){
            List<MenuEntity> menus =
                    menuRepository.findByRestaurantId(restaurantId);
            return menus.stream()
                    .map(MenuDto::fromEntity)
                    .collect(Collectors.toList());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }


    public List<MenuDto> findMenuByNameContainingAndNearby (
            String menuName,
            double userLat,
            double userLng
    ){
        List<MenuEntity> menus =
                menuRepository.findByNameContainingAndNearby(menuName, userLat, userLng);
        return menus.stream()
                .map(MenuDto::fromEntity)
                .collect(Collectors.toList());
    }








    //pick mon an vao gio hang
    //xoa mon khoi gio hang
    //thanh toan




}
