package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.common.ExceptionMessageDto;
import burgermap.dto.food.FoodAttributeDto;
import burgermap.dto.food.FoodFilter;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FoodController {

    private final FoodService foodService;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FoodAttributeNotExistException.class)
    public ExceptionMessageDto handleFoodAttributeNotExistException(FoodAttributeNotExistException e) {
        return new ExceptionMessageDto(e.getMessage());
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

    /**
     * 음식 속성 정보(메뉴 타입, 메뉴 카테고리, 재료) 조회
     * @return 음식 속성 정보 DTO
     */
    @GetMapping("foods/food-attributes")
    public ResponseEntity<FoodAttributeDto> getFoodAttributes() {
        FoodAttributeDto foodAttributeDto = new FoodAttributeDto();
        foodAttributeDto.setMenuTypes(foodService.getMenuTypes());
        foodAttributeDto.setMenuCategories(foodService.getMenuCategories().stream().map(this::cvtToMenuCategoryInfoDto).toList());
        foodAttributeDto.setIngredients(foodService.getIngredients().stream().map(this::cvtToIngredientInfoDto).toList());
        return ResponseEntity.ok(foodAttributeDto);
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

    @GetMapping("foods/{foodId}")
    public ResponseEntity<FoodInfoDto> getFood(@PathVariable Long foodId) {
        Food food = foodService.getFood(foodId);
        return ResponseEntity.ok(cvtToFoodInfoDto(food));
    }

    /**
     * 특정 가게에 등록된 모든 음식 조회
     */
    @GetMapping("stores/{storeId}/foods")
    public ResponseEntity<List<FoodInfoDto>> getStoreFoods(@PathVariable Long storeId) {
        List<Food> foods = foodService.getStoreFoods(storeId);
        List<FoodInfoDto> foodInfoDtoList = foods.stream().map(this::cvtToFoodInfoDto).toList();
        return ResponseEntity.ok(foodInfoDtoList);
    }

    /**
     * 음식 정보 수정
     */
    @PutMapping("foods/{foodId}")
    public ResponseEntity<FoodInfoDto> updateFood(
            @SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID) Long memberId,
            @PathVariable Long foodId,
            @RequestBody FoodInfoRequestDto foodInfoRequestDto) {
        Food newFoodInfo = cvtToFood(foodInfoRequestDto);
        Food newFood = foodService.updateFood(memberId, foodId, newFoodInfo);
        return ResponseEntity.ok(cvtToFoodInfoDto(newFood));
    }

    @DeleteMapping("foods/{foodId}")
    public ResponseEntity<FoodInfoDto> deleteFood(
            @SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID) Long memberId,
            @PathVariable Long foodId) {
        Food food = foodService.deleteFood(memberId, foodId);
        return ResponseEntity.ok(cvtToFoodInfoDto(food));
    }

    @GetMapping("foods/filter")
    public ResponseEntity<List<FoodInfoDto>> filterFoods(@RequestBody FoodFilter foodFilter) {
        List<Food> foods = foodService.filterFoods(foodFilter);
        List<FoodInfoDto> foodInfoDtoList = foods.stream().map(this::cvtToFoodInfoDto).toList();
        return ResponseEntity.ok(foodInfoDtoList);
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
