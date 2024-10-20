package com.example.ChoiGangDeliveryApp.owner.menu;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.menu.dto.MenuDto;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import com.example.ChoiGangDeliveryApp.owner.menu.repo.MenuRepository;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    // CREATE
    @Transactional
    public MenuDto createMenu(MenuDto menuDto) {
        MenuEntity menuEntity = MenuEntity.builder()
                .name(menuDto.getName())
                .description(menuDto.getDescription())
                .price(menuDto.getPrice())
                .cuisineType(menuDto.getCuisineType())
                .preparationTime(menuDto.getCustomEstimatedPreparationTime())
                .build();
//        MenuEntity menuEntity = MenuEntity.fromDto(menuDto); // Chuyển đổi từ DTO sang Entity
//        menuEntity = menuRepository.save(menuEntity); // Lưu vào DB
//        return MenuDto.fromEntity(menuEntity);
        log.info("loi");
        MenuEntity savedMenu = menuRepository.save(menuEntity);
        log.info("loi 2");
        return MenuDto.fromEntity(savedMenu);
    }

    // VIEW ONE BY ID
    public MenuDto getMenuById(Long id) {
        MenuEntity menuEntity = menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found with id: " + id));
        return MenuDto.fromEntity(menuEntity);
    }

    // UPDATE
    public MenuDto updateMenu(Long id, MenuDto menuDto) {
        MenuEntity menuEntity = menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));

        menuEntity.setName(menuDto.getName());
        menuEntity.setDescription(menuDto.getDescription());
        menuEntity.setPrice(menuDto.getPrice());
//        menuEntity.setImageUrl(menuDto.getImageUrl());

        MenuEntity updatedMenu = menuRepository.save(menuEntity);
        return MenuDto.fromEntity(updatedMenu);
    }

    // VIEW ALL
    public List<MenuDto> viewMenu() {
        return menuRepository.findAll().stream()
                .map(MenuDto::fromEntity)
                .collect(Collectors.toList());
    }

    // FIND BY CUISINE TYPE
    public List<MenuDto> findByCuisineType(
            CuisineType cuisineType
    ) {
        List<MenuEntity> menuEntities = menuRepository.findByCuisineType(cuisineType);
        return menuEntities.stream().map(MenuDto::fromEntity).toList();
    }

    // DELETE
    public void deleteMenu(Long id) {
        MenuEntity menuEntity = menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));
            menuRepository.delete(menuEntity);
    }
}
