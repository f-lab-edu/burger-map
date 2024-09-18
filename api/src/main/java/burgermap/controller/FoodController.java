package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.food.IngredientInfoDto;
import burgermap.dto.food.MenuCategoryInfoDto;
import burgermap.entity.Ingredient;
import burgermap.entity.MenuCategory;
import burgermap.repository.IngredientRepository;
import burgermap.service.FoodService;
import burgermap.session.SessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FoodController {

    private final FoodService foodService;

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
}
