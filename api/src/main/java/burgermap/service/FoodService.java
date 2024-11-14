package burgermap.service;

import burgermap.entity.Food;
import burgermap.entity.Ingredient;
import burgermap.entity.MenuCategory;
import burgermap.entity.Store;
import burgermap.enums.MenuType;
import burgermap.exception.food.FoodAttributeNotExistException;
import burgermap.repository.FoodRepository;
import burgermap.repository.IngredientRepository;
import burgermap.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class FoodService {
    private final StoreService storeService;
    private final MemberLookupService memberLookupService;
    private final StoreLookupService storeLookupService;

    private final IngredientRepository ingredientRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final FoodRepository foodRepository;

    public List<String> getMenuTypes() {
        return Arrays.stream(MenuType.values()).map(MenuType::getValue).toList();
    }

    public List<MenuCategory> getMenuCategories() {
        return menuCategoryRepository.findAll();
    }

    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    public Food addFood(Food food, Long storeId, Long memberId) {
        memberLookupService.isMemberTypeOwner(memberId);
        Store store = storeLookupService.findByStoreId(storeId);
        storeLookupService.checkStoreBelongTo(store, memberId);

        food.setStore(store);

        return foodRepository.save(food);
    }

    public List<Ingredient> ingredientIdsToIngredients(List<Long> ingredientIds) {
        // ingredientId, Optional<Ingredient> 맵으로 변환
        Map<Long, Optional<Ingredient>> ingredientMap = ingredientIds.stream()
                .collect(Collectors.toMap(ingredientId -> ingredientId, ingredientRepository::findByIngredientId));

        // value가 empty인 ingredientId 수집
        List<Long> notExistIngredientIdsList = ingredientMap.entrySet().stream()
                .filter(entry -> entry.getValue().isEmpty())
                .map(Map.Entry::getKey).toList();

        if (!notExistIngredientIdsList.isEmpty()) {
            String notExistIngredientIds = notExistIngredientIdsList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            log.debug("Ingredients Id {} does not exist", notExistIngredientIds);
            throw new FoodAttributeNotExistException("Ingredients Id %s does not exist".formatted(notExistIngredientIds));
        }
        return ingredientMap.values().stream().map(Optional::get).toList();
    }

    public MenuCategory menuCategoryIdToMenuCategory(Long menuCategoryId) {
        Optional<MenuCategory> menuCategory = menuCategoryRepository.findByMenuCategoryId(menuCategoryId);
        return menuCategory.orElseThrow(() -> new FoodAttributeNotExistException("Menu Category Id %d does not exist".formatted(menuCategoryId)));
    }
}
