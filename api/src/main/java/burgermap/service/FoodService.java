package burgermap.service;

import burgermap.entity.Ingredient;
import burgermap.entity.MenuCategory;
import burgermap.enums.MenuType;
import burgermap.repository.IngredientRepository;
import burgermap.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {
    private final IngredientRepository ingredientRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    public List<String> getMenuTypes() {
        return Arrays.stream(MenuType.values()).map(MenuType::getValue).toList();
    }

    public List<MenuCategory> getMenuCategories() {
        return menuCategoryRepository.findAll();
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }
}
