package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.food.FoodInfoDto;
import burgermap.dto.food.FoodInfoRequestDto;
import burgermap.dto.food.IngredientInfoDto;
import burgermap.dto.food.MenuCategoryInfoDto;
import burgermap.entity.Food;
import burgermap.entity.Ingredient;
import burgermap.entity.MenuCategory;
import burgermap.enums.MenuType;
import burgermap.exception.food.FoodAttributeNotExistException;
import burgermap.service.FoodService;
import burgermap.session.SessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FoodController {

    private final FoodService foodService;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FoodAttributeNotExistException.class)
    public Map<String, String> handleFoodAttributeNotExistException(FoodAttributeNotExistException e) {
        return Map.of("message", e.getMessage());
    }

    @GetMapping("foods/menu-types")
    public ResponseEntity<List<String>> getMenuTypes() {
        List<String> foodTypes = foodService.getMenuTypes();
        return ResponseEntity.ok(foodTypes);
    }

    @GetMapping("foods/menu-categories")
    public ResponseEntity<List<MenuCategoryInfoDto>> getMenuCategories() {
        List<MenuCategory> menuCategories = foodService.getMenuCategories();
        List<MenuCategoryInfoDto> menuCategoryInfoDtolist = menuCategories.stream().map(this::cvtToMenuCategoryInfoDto).toList();
        return ResponseEntity.ok(menuCategoryInfoDtolist);
    }

    @GetMapping("foods/ingredients")
    public ResponseEntity<List<IngredientInfoDto>> getIngredients() {
        List<Ingredient> ingredients = foodService.getIngredients();
        List<IngredientInfoDto> ingredientInfoDtoList = ingredients.stream().map(this::cvtToIngredientInfoDto).toList();
        return ResponseEntity.ok(ingredientInfoDtoList);
    }

    @CheckLogin
    @PostMapping("stores/{storeId}/foods")
    public ResponseEntity<FoodInfoDto> addFood(
            @SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID) Long memberId,
            @PathVariable Long storeId,
            @RequestBody FoodInfoRequestDto foodInfoRequestDto) {
        Food food = cvtToFood(foodInfoRequestDto);
        Food addedFood = foodService.addFood(food, storeId, memberId);
        return ResponseEntity.ok(cvtToFoodInfoDto(addedFood));
    }

    public MenuCategoryInfoDto cvtToMenuCategoryInfoDto(MenuCategory menuCategory) {
        MenuCategoryInfoDto menuCategoryInfoDto = new MenuCategoryInfoDto();
        menuCategoryInfoDto.setMenuCategoryId(menuCategory.getMenuCategoryId());
        menuCategoryInfoDto.setName(menuCategory.getName());
        return menuCategoryInfoDto;
    }

    public IngredientInfoDto cvtToIngredientInfoDto(Ingredient ingredient) {
        IngredientInfoDto ingredientInfoDto = new IngredientInfoDto();
        ingredientInfoDto.setIngredientId(ingredient.getIngredientId());
        ingredientInfoDto.setName(ingredient.getName());
        return ingredientInfoDto;
    }

    public Food cvtToFood(FoodInfoRequestDto foodInfoRequestDto) {
        Food food = new Food();
        food.setName(foodInfoRequestDto.getName());
        food.setPrice(foodInfoRequestDto.getPrice());
        food.setDescription(foodInfoRequestDto.getDescription());
        food.setMenuType(MenuType.from(foodInfoRequestDto.getMenuTypeValue()));
        food.setMenuCategory(foodService.menuCategoryIdToMenuCategory(foodInfoRequestDto.getMenuCategoryId()));
        food.setIngredients(foodService.ingredientIdsToIngredients(foodInfoRequestDto.getIngredientIds()));
        return food;
    }

    public FoodInfoDto cvtToFoodInfoDto(Food food) {
        FoodInfoDto foodInfoDto = new FoodInfoDto();
        foodInfoDto.setFoodId(food.getFoodId());
        foodInfoDto.setStoreId(food.getStore().getStoreId());
        foodInfoDto.setName(food.getName());
        foodInfoDto.setPrice(food.getPrice());
        foodInfoDto.setDescription(food.getDescription());
        foodInfoDto.setMenuTypeValue(food.getMenuType().getValue());
        foodInfoDto.setMenuCategory(food.getMenuCategory());
        foodInfoDto.setIngredients(food.getIngredients());
        return foodInfoDto;
    }
}
