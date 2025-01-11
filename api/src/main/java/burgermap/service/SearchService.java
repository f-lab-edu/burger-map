package burgermap.service;

import burgermap.dto.food.FoodFilter;
import burgermap.dto.geo.GeoLocationRange;
import burgermap.dto.store.StoresWithFoodsEntityWrapper;
import burgermap.entity.Food;
import burgermap.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final StoreLookupService storeLookupService;
    private final FoodLookupService foodLookupService;

    /*
     * 음식 필터를 만족하는 음식을 판매하며, 주어진 위경도 범위를 만족하는 가게를 조회
     *
     * @param geoLocationRange 최소/최대 위경도를 포함하는 객체
     * @param foodFilter 음식 필터(음식 카테고리, 포함 재료)
     */
    public StoresWithFoodsEntityWrapper searchStores(GeoLocationRange geoLocationRange, FoodFilter foodFilter) {
        // 위경도 범위를 만족하는 가게 조회
        List<Store> stores = storeLookupService.getStoresWithinGeoRange(geoLocationRange);
        List<Food> foods = new ArrayList<>();

        // 음식 필터가 유효한지 확인
        boolean isFilterValid = foodFilter.getMenuCategoryId() != null || !foodFilter.getIngredientIds().isEmpty();
        if (!stores.isEmpty() && isFilterValid) {
            // 음식 필터에 가게 조건 추가
            List<Long> storeIds = stores.stream().map(Store::getStoreId).toList();
            foodFilter.setStoreIds(storeIds);

            // 음식 필터를 만족하는 음식을 조회하고 해당 음식을 판매하는 가게만 필터링
            foods.addAll(foodLookupService.filterFoods(foodFilter));
            List<Long> filteredStoreIds = foods.stream().map(food -> food.getStore().getStoreId()).distinct().toList();
            stores.removeIf(store -> !filteredStoreIds.contains(store.getStoreId()));
        }
        return new StoresWithFoodsEntityWrapper(stores, foods);
    }
}
