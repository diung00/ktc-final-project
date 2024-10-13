package com.example.ChoiGangDeliveryApp.user.service;

import com.example.ChoiGangDeliveryApp.api.ncpmaps.NaviService;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.PointDto;
import com.example.ChoiGangDeliveryApp.owner.menu.dto.MenuDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;

import com.example.ChoiGangDeliveryApp.user.dto.UpdateAddressDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final UserRepository userRepository;
    private final AuthenticationFacade authFacade;
    private final NaviService naviService;
    private final RestaurantRepository restaurantRepository;

    public UserDto customerUpdateAddress(UpdateAddressDto dto){
        UserEntity userEntity = authFacade.getCurrentUserEntity();

        userEntity.setAddress(dto.getAddress());

        userRepository.save(userEntity);

        return UserDto.fromEntity(userEntity);
    }



    // lấy danh sách nhà hàng trong vòng 3km so với địa chỉ đc nhập vào
    public List<RestaurantDto> getRestaurantInMyArea(String address){

        PointDto location = naviService.geoLocation(address);

        List<RestaurantsEntity> restaurants =
                restaurantRepository.findRestaurantsWithinRadius(location.getLatitude(), location.getLongitude(), 3);
        return restaurants.stream()
                .map(RestaurantDto::fromEntity)
                .collect(Collectors.toList());
    }

   // xem một nhà hàng

    public RestaurantDto getRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
               .map(RestaurantDto::fromEntity)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    }

    // xem danh sách món ăn của 1 nhà hàng

    public List<MenuDto> menuDtoList(Long restaurantId){
        return restaurantRepository.findById(restaurantId)
               .map(RestaurantsEntity::getMenus)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"))
               .stream()
               .map(MenuDto::fromEntity)
               .collect(Collectors.toList());
    }

    //xem 1 món ăn

    public MenuDto getMenu(Long restaurantId, Long menuId){
        return restaurantRepository.findById(restaurantId)
               .map(restaurantsEntity -> restaurantsEntity.getMenus().stream()
                       .filter(menu -> menu.getId().equals(menuId))
                       .findFirst()
                       .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found")))
               .map(MenuDto::fromEntity)
               .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    }

    // hủy đơn khi nhà hàng chưa chấp nhận đơn

    /*public void cancelOrder(Long restaurantId, Long orderId){
        UserEntity user = authFacade.getCurrentUserEntity();
        user.
    }*/







}
