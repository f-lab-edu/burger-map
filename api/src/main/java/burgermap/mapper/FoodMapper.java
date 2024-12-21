package burgermap.mapper;

import burgermap.dto.food.FoodInfoDto;
import burgermap.dto.food.FoodInfoRequestDto;
import burgermap.dto.food.IngredientInfoDto;
import burgermap.dto.food.MenuCategoryInfoDto;
import burgermap.entity.Food;
import burgermap.entity.Ingredient;
import burgermap.entity.MenuCategory;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {
    public Food fromDto(FoodInfoRequestDto foodInfoRequestDto) {
        return Food.builder()
                .name(foodInfoRequestDto.getName())
                .price(foodInfoRequestDto.getPrice())
                .description(foodInfoRequestDto.getDescription())
                .menuTypeValue(foodInfoRequestDto.getMenuTypeValue())
                .build();
    }

    public FoodInfoDto toFoodInfoDto(Food food) {
        return FoodInfoDto.builder()
                .foodId(food.getFoodId())
                .storeId(food.getStore().getStoreId())
                .name(food.getName())
                .price(food.getPrice())
                .description(food.getDescription())
                .menuTypeValue(food.getMenuType().getValue())
                .menuCategory(food.getMenuCategory())
                .ingredients(food.getIngredients())
                .build();
    }

    public MenuCategoryInfoDto toMenuCategoryInfoDto(MenuCategory menuCategory) {
        return MenuCategoryInfoDto.builder()
                .menuCategoryId(menuCategory.getMenuCategoryId())
                .name(menuCategory.getName())
                .build();
    }

    public IngredientInfoDto toIngredientInfoDto(Ingredient ingredient) {
        return IngredientInfoDto.builder()
                .ingredientId(ingredient.getIngredientId())
                .name(ingredient.getName())
                .build();
    }
}
