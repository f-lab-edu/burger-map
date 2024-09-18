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
    private final IngredientRepository ingredientRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final FoodRepository foodRepository;

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

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
        Member member = memberRepository.findByMemberId(memberId).get();
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }
        Store oldStore = storeRepository.findByStoreId(storeId).orElse(null);
        if (oldStore == null) {  // 존재하지 않는 가게
            throw new StoreNotExistException(storeId);
        }
        else if (!oldStore.getMemberId().equals(memberId)) {  // 요청자가 가게의 소유자가 아님
            throw new NotOwnerMemberException("member is not owner of the store.");
        }

        return foodRepository.save(food);
    }
}
