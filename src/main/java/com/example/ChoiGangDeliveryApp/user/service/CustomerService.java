package com.example.ChoiGangDeliveryApp.user.service;

import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;

import com.example.ChoiGangDeliveryApp.user.dto.UpdateAddressDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final UserRepository userRepository;
    private final AuthenticationFacade authFacade;

    public UserDto customerUpdateAddress(UpdateAddressDto dto){
        UserEntity userEntity = authFacade.getCurrentUserEntity();

        userEntity.setAddress(dto.getAddress());

        userRepository.save(userEntity);

        return UserDto.fromEntity(userEntity);
    }

}
