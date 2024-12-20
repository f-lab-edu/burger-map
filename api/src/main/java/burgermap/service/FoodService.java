package burgermap.service;

import burgermap.dto.food.FoodFilter;
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
@Slf4j
@RequiredArgsConstructor
public class FoodService {
    private final FoodLookupService foodLookupService;
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

    @Transactional
    public Food addFood(Food food, Long menuCategoryId, List<Long> ingredientIds, Long storeId, Long memberId) {
        memberLookupService.isMemberTypeOwner(memberId);
        Store store = storeLookupService.findByStoreId(storeId);
        storeLookupService.checkStoreBelongTo(store, memberId);

        // 카테고리와 재료 조회 후 음식 엔티티에 주입
        setFoodAttributes(food, menuCategoryId, ingredientIds);
        food.setStore(store);

        return foodRepository.save(food);
    }

    /**
     * 음식 엔티티에 카테고리와 재료 주입
     */
    public void setFoodAttributes(Food food, Long menuCategoryId, List<Long> ingredientIds) {
        food.setMenuCategory(menuCategoryIdToMenuCategory(menuCategoryId));
        food.setIngredients(ingredientIdsToIngredients(ingredientIds));
    }

    public Food getFood(Long foodId) {
        Food food = foodLookupService.findByFoodId(foodId);
        log.debug("food info: {}", food);
        return food;
    }

    /**
     * 특정 가게 엔티티와 관계된 모든 음식 엔티티 조회
     */
    public List<Food> getStoreFoods(Long storeId) {
        storeLookupService.validateStoreExists(storeId);
        List<Food> foods = foodLookupService.findByStoreId(storeId);
        log.debug("store {} - foods: {}", storeId, foods);
        return foods;
    }

    /**
     * 음식 엔티티 수정
     * 요청 회원이 음식이 등록된 가게의 소유자 여부 확인, 음식 엔티티 수정
     */
    @Transactional
    public Food updateFood(Long requestMemberId, Long foodId, Food newFoodInfo, Long menuCategoryId, List<Long> ingredientIds) {
        Food food = foodLookupService.findByFoodId(foodId);
        storeLookupService.checkStoreBelongTo(food.getStore(), requestMemberId);

        // 카테고리와 재료 주입
        setFoodAttributes(newFoodInfo, menuCategoryId, ingredientIds);

        // 엔티티 정보 수정
        food.setName(newFoodInfo.getName());
        food.setPrice(newFoodInfo.getPrice());
        food.setDescription(newFoodInfo.getDescription());
        food.setMenuType(newFoodInfo.getMenuType());
        food.setMenuCategory(newFoodInfo.getMenuCategory());
        food.setIngredients(newFoodInfo.getIngredients());

        return food;
    }

    @Transactional
    public Food deleteFood(Long requestMemberId, Long foodId) {
        Food food = foodLookupService.findByFoodId(foodId);
        storeLookupService.checkStoreBelongTo(food.getStore(), requestMemberId);
        foodRepository.delete(food);
        log.debug("food deleted: {}", food);
        return food;
    }

    public List<Food> filterFoods(FoodFilter foodFilter) {
        return foodRepository.filterFood(foodFilter);
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
