package com.example.ChoiGangDeliveryApp.user.controller;

import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;
import com.example.ChoiGangDeliveryApp.user.dto.UpdateAddressDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;
    private final AuthenticationFacade authFacade;

    @PutMapping("updateAddress")
    public UserDto update(
            @RequestBody
            UpdateAddressDto dto
    ){
        customerService.customerUpdateAddress(dto);
        UserEntity entity = authFacade.getCurrentUserEntity();
        return UserDto.fromEntity(entity);
    }
}
