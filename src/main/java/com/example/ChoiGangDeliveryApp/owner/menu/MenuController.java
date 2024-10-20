package com.example.ChoiGangDeliveryApp.owner.menu;

import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.menu.dto.MenuDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> getMenuById(
            @PathVariable
            Long id
    ) {
        MenuDto menuDto = menuService.getMenuById(id);
        return ResponseEntity.ok(menuDto);
    }

    // UPDATE MENU
    @PutMapping("/update/{id}")
    public ResponseEntity<MenuDto> updateMenu(
            @PathVariable
            Long id,
            @RequestBody
            MenuDto menuDto
    ) {
        MenuDto updatedMenu = menuService.updateMenu(id, menuDto);
        return ResponseEntity.ok(updatedMenu);
    }

    // VIEW MENU
    @GetMapping("/view-all-menu")
    public ResponseEntity<List<MenuDto>> viewAllMenu() {
        List<MenuDto> menus = menuService.viewMenu();
        return ResponseEntity.ok(menus);
    }

    // FIND BY CUISINE TYPE
    @GetMapping("/find-by-cuisine-type")
    public ResponseEntity<List<MenuDto>> findByCuisineType(
            @RequestParam
            CuisineType cuisineType) {
        List<MenuDto> menus = menuService.findByCuisineType(cuisineType);
        return ResponseEntity.ok(menus);
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

















