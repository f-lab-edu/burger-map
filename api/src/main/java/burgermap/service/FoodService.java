package burgermap.service;

import burgermap.entity.Food;
import burgermap.entity.Ingredient;
import burgermap.entity.Member;
import burgermap.entity.MenuCategory;
import burgermap.entity.Store;
import burgermap.enums.MemberType;
import burgermap.enums.MenuType;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.exception.store.StoreNotExistException;
import burgermap.repository.FoodRepository;
import burgermap.repository.IngredientRepository;
import burgermap.repository.MemberRepository;
import burgermap.repository.MenuCategoryRepository;
import burgermap.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodService {
    private final MemberService memberService;
    private final StoreService storeService;

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
        memberService.isMemberTypeOwner(memberId);
        Store store = storeService.checkStoreExistence(storeId);
        storeService.checkStoreBelongTo(store, memberId);

        return foodRepository.save(food);
    }
}
