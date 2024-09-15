package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.food.BurgerInfoDto;
import burgermap.dto.food.BurgerInfoRequestDto;
import burgermap.dto.food.SideMenuInfoDto;
import burgermap.dto.food.SideMenuInfoRequestDto;
import burgermap.entity.Burger;
import burgermap.entity.Food;
import burgermap.entity.SideMenu;
import burgermap.enums.FoodType;
import burgermap.service.FoodService;
import burgermap.session.SessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FoodController {

    private final FoodService foodService;

    @CheckLogin
    @PostMapping("/stores/{storeId}/burgers")
    public ResponseEntity<BurgerInfoDto> addBurger(@SessionAttribute(name = SessionConstants.loginMember) Long memberId,
                                                   @PathVariable Long storeId,
                                                   @RequestBody BurgerInfoRequestDto burgerInfoRequestDto) {
        Burger burger = cvtToBurger(burgerInfoRequestDto, storeId);
        foodService.addBurger(memberId, burger);
        return ResponseEntity.ok(cvtToBurgerInfoDto(burger));
    }

    @CheckLogin
    @PostMapping("/stores/{storeId}/side-menus")
    public ResponseEntity<SideMenuInfoDto> addSideMenu(@SessionAttribute(name = SessionConstants.loginMember) Long memberId,
                                                     @PathVariable Long storeId,
                                                     @RequestBody SideMenuInfoRequestDto sideMenuInfoRequestDto) {
        SideMenu sideMenu = cvtToSideMenu(sideMenuInfoRequestDto, storeId);
        foodService.addSideMenu(memberId, sideMenu);
        return ResponseEntity.ok(cvtToSideMenuInfoDto(sideMenu));
    }

    @GetMapping("/stores/{storeId}/foods")
    public ResponseEntity<List<Food>> getFoods(@PathVariable Long storeId) {
        List<Food> foods = foodService.getFoods(storeId);
        return ResponseEntity.ok(foods);
    }

    public Burger cvtToBurger(Object dto, Long storeId) {
        Burger burger = new Burger();

        if (dto instanceof BurgerInfoRequestDto burgerInfoRequestDto) {
            burger.setName(burgerInfoRequestDto.getName());
            burger.setPrice(burgerInfoRequestDto.getPrice());
            burger.setDescription(burgerInfoRequestDto.getDescription());
            burger.setBunType(burgerInfoRequestDto.getBunType());
            burger.setPattyType(burgerInfoRequestDto.getPattyType());
            burger.setStoreId(storeId);
        }

        return burger;
    }

    public SideMenu cvtToSideMenu(Object dto, Long storeId) {
        SideMenu sideMenu = new SideMenu();

        if (dto instanceof SideMenuInfoRequestDto sideMenuInfoRequestDto) {
            sideMenu.setName(sideMenuInfoRequestDto.getName());
            sideMenu.setPrice(sideMenuInfoRequestDto.getPrice());
            sideMenu.setDescription(sideMenuInfoRequestDto.getDescription());
            sideMenu.setSideMenuType(sideMenuInfoRequestDto.getSideMenuType());
            sideMenu.setStoreId(storeId);
        }

        return sideMenu;
    }

    public BurgerInfoDto cvtToBurgerInfoDto(Burger burger) {
        BurgerInfoDto burgerInfoDto = new BurgerInfoDto();
        burgerInfoDto.setFoodId(burger.getFoodId());
        burgerInfoDto.setStoreId(burger.getStoreId());
        burgerInfoDto.setName(burger.getName());
        burgerInfoDto.setPrice(burger.getPrice());
        burgerInfoDto.setDescription(burger.getDescription());
        burgerInfoDto.setBunType(burger.getBunType());
        burgerInfoDto.setPattyType(burger.getPattyType());
        burgerInfoDto.setFoodType(FoodType.BURGER);
        return burgerInfoDto;
    }

    public SideMenuInfoDto cvtToSideMenuInfoDto(SideMenu sideMenu) {
        SideMenuInfoDto sideMenuInfoDto = new SideMenuInfoDto();
        sideMenuInfoDto.setFoodId(sideMenu.getFoodId());
        sideMenuInfoDto.setStoreId(sideMenu.getStoreId());
        sideMenuInfoDto.setName(sideMenu.getName());
        sideMenuInfoDto.setPrice(sideMenu.getPrice());
        sideMenuInfoDto.setDescription(sideMenu.getDescription());
        sideMenuInfoDto.setSideMenuType(sideMenu.getSideMenuType());
        sideMenuInfoDto.setFoodType(FoodType.SIDE);
        return sideMenuInfoDto;
    }

}
