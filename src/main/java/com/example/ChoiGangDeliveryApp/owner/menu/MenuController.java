package com.example.ChoiGangDeliveryApp.owner.menu;

import com.example.ChoiGangDeliveryApp.owner.menu.dto.MenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    // CREATE MENU
    @PostMapping("/create")
    public ResponseEntity<MenuDto> createMenu(
            @RequestBody
            MenuDto menuDto
    ) {
        MenuDto createdMenu = menuService.createMenu(menuDto);
        return ResponseEntity.ok(createdMenu);
    }

    // VIEW MENU BY MENU ID
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDto> getMenuById(
            @PathVariable
            Long menuId
    ) {
        MenuDto menuDto = menuService.getMenuById(menuId);
        return ResponseEntity.ok(menuDto);
    }

    // VIEW ALL MENU BY RESTAURANT ID
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuDto>> viewAllMenuByRestaurantId(
            @PathVariable Long restaurantId
    ) {
        List<MenuDto> menus = menuService.viewAllMenuByRestaurantId(restaurantId);
        return ResponseEntity.ok(menus);
    }

    // UPDATE MENU
    @PutMapping("/update/{menuId}")
    public ResponseEntity<MenuDto> updateMenu(
            @PathVariable
            Long menuId,
            @RequestBody
            MenuDto menuDto
    ) {
        MenuDto updatedMenu = menuService.updateMenu(menuId, menuDto);
        return ResponseEntity.ok(updatedMenu);
    }


    //UPDATE MENU IMAGE
    @PutMapping("/{menuId}/image")
    public ResponseEntity<MenuDto> updateMenuImage(
            @PathVariable
            Long menuId,
            @RequestParam("image")
            MultipartFile image
    ) throws Exception {
        MenuDto updatedMenu = menuService.updateMenuImage(menuId, image);
        return ResponseEntity.ok(updatedMenu);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMenu(
            @PathVariable Long id
    ) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }
}

















