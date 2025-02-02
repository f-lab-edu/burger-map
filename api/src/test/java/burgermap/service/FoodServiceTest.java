package burgermap.service;

import burgermap.entity.Food;
import burgermap.entity.Ingredient;
import burgermap.entity.Member;
import burgermap.entity.MenuCategory;
import burgermap.entity.Store;
import burgermap.enums.MenuType;
import burgermap.exception.food.FoodAttributeNotExistException;
import burgermap.repository.FoodRepository;
import burgermap.repository.IngredientRepository;
import burgermap.repository.MenuCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {
    @InjectMocks
    FoodService foodService;
    @Mock
    FoodLookupService foodLookupService;
    @Mock
    MemberLookupService memberLookupService;
    @Mock
    StoreLookupService storeLookupService;
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    MenuCategoryRepository menuCategoryRepository;
    @Mock
    FoodRepository foodRepository;

    /**
     * IngredientRepository mock의 동작을 정의
     * 주어진 재료 ID를 가진 재료 객체를 생성하고
     * 재료 ID와 해당 객체를 연관시키는 mock 동작 정의.
     */
    List<Ingredient> defineIngredientRepoMockAction(List<Long> ingredientIds) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (Long ingredientId : ingredientIds) {
            // 재료 객체 생성
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientId(ingredientId);
            ingredients.add(ingredient);
            // mock 동작 정의
            when(ingredientRepository.findByIngredientId(ingredientId))
                    .thenReturn(Optional.of(ingredients.get(ingredients.size()-1)));
        }
        return ingredients;
    }

    /**
     * MenuCategoryRepository mock의 동작 정의
     * 주어진 메뉴 카테고리 ID를 가진 메뉴 카테고리 객체를 생성하고
     * 메뉴 카테고리 ID와 해당 객체를 연관시키는 mock 동작 정의.
     */
    MenuCategory defineMenuCategoryRepoMockAction(long menuCategoryId) {
        // 메뉴 카테고리 객체 생성
        MenuCategory menuCategory = new MenuCategory();
        menuCategory.setMenuCategoryId(menuCategoryId);
        // mock 동작 정의
        when(menuCategoryRepository.findByMenuCategoryId(menuCategoryId))
                .thenReturn(Optional.of(menuCategory));
        return menuCategory;
    }

    @Test
    @DisplayName("여러 식재료 조회 시 재료 객체의 리스트를 반환함.")
    void ingredientIdsToIngredients_returnListOfIngredients() {
        // given
        // 재료 등록
        List<Long> ingredientIds = List.of(1L, 2L, 3L);
        List<Ingredient> ingredients = defineIngredientRepoMockAction(ingredientIds);

        // when
        List<Ingredient> foundIngredients = foodService.ingredientIdsToIngredients(ingredientIds);

        // then
        // 개별 재료 조회 결과를 리스트로 반환하는지 검증
        assertThat(foundIngredients).containsAll(ingredients);
    }

    @Test
    @DisplayName("여러 식재료 조회 시 등록되지 않은 재료 ID가 존재하면 FoodAttributeNotExistException 발생")
    void ingredientIdsToIngredients_whenFindUnregisteredIngredientThrowException() {
        // given
        // 재료 등록
        List<Long> ingredientIds = new ArrayList<>(List.of(1L, 2L, 3L));
        defineIngredientRepoMockAction(ingredientIds);
        // 등록되지 않은 재료 ID를 추가
        // mock에 특정 값에 대한 동작을 정의하지 않은 경우 메서드 반환형을 반영해 null 또는 Optional.empty 반환함.
        List<Long> unregisteredIngredientIds = new ArrayList<>(List.of(4L, 99L));
        ingredientIds.addAll(unregisteredIngredientIds);

        // when
        FoodAttributeNotExistException foodAttributeNotExistException = assertThrows(
                FoodAttributeNotExistException.class, () -> foodService.ingredientIdsToIngredients(ingredientIds));
        // 예외 메세지에 등록되지 않은 재료 ID를 명시하는지 검증
        String actualMessage = foodAttributeNotExistException.getMessage();
        Pattern digitPattern = Pattern.compile("\\d+");
        Matcher digitMatcher = digitPattern.matcher(actualMessage);
        List<Long> exceptionIngredientIds = new ArrayList<>();
        while (digitMatcher.find()) {
            exceptionIngredientIds.add(Long.parseLong(digitMatcher.group()));
        }
        assertThat(exceptionIngredientIds).containsAll(unregisteredIngredientIds);
    }

    @Test
    @DisplayName("메뉴 카테고리 조회 시 등록되지 않은 메뉴 카테고리인 경우 FoodAttributeNotExistException 발생")
    void menuCategoryIdToMenuCategory_whenFindUnregisteredMenuCategoryThrowException() {
        // given
        long unregisteredMenuCategoryId = 99L;

        // when
        // then
        FoodAttributeNotExistException foodAttributeNotExistException = assertThrows(
                FoodAttributeNotExistException.class, () -> foodService.menuCategoryIdToMenuCategory(unregisteredMenuCategoryId));
        // 예외 메세지에 등록되지 않은 카테고리 ID를 명시하는지 검증
        String actualMessage = foodAttributeNotExistException.getMessage();
        Pattern digitPattern = Pattern.compile("\\d+");
        Matcher digitMatcher = digitPattern.matcher(actualMessage);
        List<Long> exceptionMenuCategoryIds = new ArrayList<>();
        while (digitMatcher.find()) {
            exceptionMenuCategoryIds.add(Long.parseLong(digitMatcher.group()));
        }
        assertThat(exceptionMenuCategoryIds).contains(unregisteredMenuCategoryId);
    }

    @Test
    @DisplayName("음식 객체에 메뉴 카테고리, 재료를 조회해 주입함.")
    void setFoodAttributes_injectsMenuCategoryAndIngredients() {
        // given
        // 카테고리 등록
        long menuCategoryId = 1L;
        MenuCategory menuCategory = defineMenuCategoryRepoMockAction(menuCategoryId);
        // 재료 등록
        List<Long> ingredientIds = List.of(1L, 2L, 3L);
        List<Ingredient> ingredients = defineIngredientRepoMockAction(ingredientIds);

        Food food = new Food();

        // when
        foodService.setFoodAttributes(food, menuCategoryId, ingredientIds);

        // then
        // 음식 객체에 메뉴 카테고리, 재료 리스트가 주입되었는지 검증
        assertThat(food.getMenuCategory()).isEqualTo(menuCategory);
        assertThat(food.getIngredients()).containsAll(ingredients);
    }
    
    @Test
    @DisplayName("음식 정보 수정 시 기존 정보를 새로운 정보로 갱신함.")
    void updateFood_updatesFoodInfos() {
        // given
        // 기존 정보 정의
        long requesterMemberId = 1L;
        Member requester = new Member();
        Store store = new Store();
        store.setMember(requester);

        Food food = new Food();
        food.setFoodId(1L);
        food.setName("oldFoodName");
        food.setStore(store);
        food.setPrice(10000);
        food.setDescription("oldFoodDescription");
        MenuType oldMenuType = MenuType.MAIN;
        food.setMenuType(oldMenuType);
        MenuCategory oldMenuCategory = new MenuCategory();
        food.setMenuCategory(oldMenuCategory);
        List<Ingredient> oldIngredients = List.of(new Ingredient());
        food.setIngredients(oldIngredients);

        // 새로운 정보 정의
        long newMenuCategoryId = 99L;
        MenuCategory newMenuCategory = defineMenuCategoryRepoMockAction(newMenuCategoryId);
        List<Long> newIngredientIds = List.of(1L, 2L);
        List<Ingredient> newIngredients = defineIngredientRepoMockAction(newIngredientIds);
        MenuType newMenuType = MenuType.SIDE;

        Food newFoodInfo = new Food();
        newFoodInfo.setName("newFoodName");
        newFoodInfo.setPrice(20000);
        newFoodInfo.setDescription("newFoodDescription");
        newFoodInfo.setMenuType(newMenuType);

        // mock 동작 정의

        when(foodLookupService.findByFoodId(food.getFoodId()))
                .thenReturn(food);
        doNothing()
                .when(storeLookupService).checkStoreBelongTo(store, requesterMemberId);

        // when
        foodService.updateFood(requesterMemberId, food.getFoodId(), newFoodInfo, newMenuCategoryId, newIngredientIds);

        // then
        // 음식 정보가 새로운 정보로 갱신되었는지 검증
        assertThat(food.getName()).isEqualTo(newFoodInfo.getName());
        assertThat(food.getPrice()).isEqualTo(newFoodInfo.getPrice());
        assertThat(food.getDescription()).isEqualTo(newFoodInfo.getDescription());
        assertThat(food.getMenuType()).isEqualTo(newFoodInfo.getMenuType());
        assertThat(food.getMenuCategory()).isEqualTo(newMenuCategory);
        assertThat(food.getIngredients()).containsAll(newIngredients);
    }

    @Test
    @DisplayName("음식 등록 시 음식 객체에 가게 객체, 카테고리, 재료를 주입함.")
    void addFood_injectsStoreFoodCategoryAndIngredients() {
        // given
        Food food = new Food();
        // 카테고리 등록
        long menuCategoryId = 1L;
        MenuCategory menuCategory = defineMenuCategoryRepoMockAction(menuCategoryId);
        // 재료 등록
        List<Long> ingredientIds = List.of(1L, 2L);
        List<Ingredient> ingredients = defineIngredientRepoMockAction(ingredientIds);
        // 음식을 등록하는 가게 생성
        long storeId = 1L;
        Store store = new Store();
        when(storeLookupService.findByStoreId(storeId))
                .thenReturn(store);
        // 음식을 등록하는 회원 생성
        long memberId = 1L;
        when(memberLookupService.isMemberTypeOwner(memberId))
                .thenReturn(new Member());
        doNothing()
                .when(storeLookupService).checkStoreBelongTo(store, memberId);
        when(foodRepository.save(food))
                .thenReturn(food);

        // when
        foodService.addFood(food, menuCategoryId, ingredientIds, storeId, memberId);

        // then
        // 음식 객체에 가게 객체와 메뉴 카테고리, 재료 리스트가 주입되었는지 검증
        assertThat(food.getStore()).isEqualTo(store);
        assertThat(food.getMenuCategory()).isEqualTo(menuCategory);
        assertThat(food.getIngredients()).containsAll(ingredients);
    }
}
