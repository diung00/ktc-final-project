package com.example.ChoiGangDeliveryApp.owner.menu;

import com.example.ChoiGangDeliveryApp.common.exception.GlobalErrorCode;
import com.example.ChoiGangDeliveryApp.common.exception.GlobalException;
import com.example.ChoiGangDeliveryApp.common.file.FileService;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.owner.menu.dto.CreateMenuDto;
import com.example.ChoiGangDeliveryApp.owner.menu.dto.MenuDto;
import com.example.ChoiGangDeliveryApp.owner.menu.entity.MenuEntity;
import com.example.ChoiGangDeliveryApp.owner.menu.repo.MenuRepository;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final AuthenticationFacade facade;
    private final RestaurantRepository restaurantRepository;
    private final FileService fileService;

    // CREATE
    @Transactional
    public MenuDto createMenu(CreateMenuDto dto) {
        UserEntity currentUser = facade.getCurrentUserEntity();

        //check restaurant status
        RestaurantsEntity restaurant = restaurantRepository.findByOwner(currentUser)
                .orElseThrow(()->new EntityNotFoundException("Restaurant not found for the current user."));

        checkRestaurantStatus(restaurant.getId());

        MenuEntity menuEntity = MenuEntity.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .menuStatus(dto.getMenuStatus())
                .cuisineType(dto.getCuisineType())
                .preparationTime(dto.getPreparationTime())
                .restaurant(restaurant)
                .build();

        MenuEntity savedMenu = menuRepository.save(menuEntity);
        return MenuDto.fromEntity(savedMenu);
    }

    //Update MenuImg
    public MenuDto updateMenuImage(Long menuId, MultipartFile image) throws Exception {
        // 1. Get current user
        UserEntity currentUser = facade.getCurrentUserEntity();

        // 2. Check if the menu exists
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));

        // 3. Check if the user is the owner of the restaurant that this menu belongs to
        RestaurantsEntity restaurant = menu.getRestaurant();
        if (!restaurant.getOwner().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of the restaurant.");
        }

        // 4. Determine the upload directory for the menu image
        String menuDirectory = "media/restaurants/" + restaurant.getId() + "/menus/" + menuId + "/";

        // Check and create the menu image folder if it doesn't exist
        try {
            Files.createDirectories(Path.of(menuDirectory));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating menu directory", e);
        }

        // 5. Delete the old image if it exists
        String oldImagePath = menu.getImagePath();
        if (oldImagePath != null) {
            Path oldImage = Path.of("src/main/resources/static" + oldImagePath);
            try {
                Files.deleteIfExists(oldImage);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting old menu image", e);
            }
        }

        // 6. Save the image file by FileService and get saved name
        String savedFileName;
        try {
            savedFileName = fileService.uploadFile(menuDirectory, image.getOriginalFilename(), image.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving menu image", e);
        }

        // 7. Set the image path for the menu
        String reqPath = "/static/restaurants/" + restaurant.getId() + "/menus/" + menuId + "/" + savedFileName;
        menu.setImagePath(reqPath);

        // 8. Save and return updated MenuDto
        MenuEntity updatedMenu = menuRepository.save(menu);
        return MenuDto.fromEntity(updatedMenu);
    }

    // UPDATE
    public MenuDto updateMenu(Long menuId, MenuDto menuDto) {
        MenuEntity menuEntity = menuRepository.findById(menuId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));

        menuEntity.setName(menuDto.getName());
        menuEntity.setPrice(menuDto.getPrice());
        menuEntity.setDescription(menuDto.getDescription());
        menuEntity.setMenuStatus(menuDto.getMenuStatus());
        menuEntity.setPreparationTime(menuDto.getPreparationTime());
        menuEntity.setCuisineType(menuDto.getCuisineType());

        MenuEntity updatedMenu = menuRepository.save(menuEntity);
        return MenuDto.fromEntity(updatedMenu);
    }

    // VIEW ONE BY ID
    public MenuDto getMenuById(Long id) {
        MenuEntity menuEntity = menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found with id: " + id));
        return MenuDto.fromEntity(menuEntity);
    }

    // VIEW ALL MENU BY RESTAURANT ID
    public List<MenuDto> viewAllMenuByRestaurantId(Long restaurantId) {
        return menuRepository.findByRestaurantId(restaurantId).stream()
                .map(MenuDto::fromEntity)
                .collect(Collectors.toList());
    }

    // DELETE
    public void deleteMenu(Long id) {
        MenuEntity menuEntity = menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));
            menuRepository.delete(menuEntity);
    }

    private RestaurantsEntity checkRestaurantStatus(Long restaurantId) {
        RestaurantsEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(()-> new GlobalException(GlobalErrorCode.RESTAURANT_NOT_FOUND));
        if (!restaurant.getApprovalStatus().equals(ApprovalStatus.APPROVED))
            throw new GlobalException(GlobalErrorCode.RESTAURANT_NOT_APPROVED);
        return restaurant;
    }
}
