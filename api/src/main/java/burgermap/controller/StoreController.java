package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.food.FoodFilter;
import burgermap.dto.geo.GeoLocationRange;
import burgermap.dto.store.StoreInfoDto;
import burgermap.dto.store.StoreRequestDto;
import burgermap.dto.store.StoreSearchResultDto;
import burgermap.entity.Food;
import burgermap.entity.Store;
import burgermap.mapper.StoreMapper;
import burgermap.service.FoodService;
import burgermap.service.StoreService;
import burgermap.session.SessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("stores")
public class StoreController {

    private final StoreService storeService;
    private final FoodService foodService;

    private final StoreMapper storeMapper;

    /**
     * 가게 추가
     */
    @CheckLogin
    @PostMapping
    public ResponseEntity<StoreInfoDto> addStore(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID, required = false) Long memberId,
                                                 @RequestBody StoreRequestDto storeRequestDto) {
        Store store = storeMapper.fromDto(storeRequestDto);
        storeService.addStore(store, memberId);
        System.out.println("store = " + store);
        return ResponseEntity.ok(storeMapper.toStoreInfoDto(store));
    }

    /**
     * 특정 가게 조회
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreInfoDto> getStore(@PathVariable Long storeId) {
        Store store = storeService.getStore(storeId);
        return ResponseEntity.ok(storeMapper.toStoreInfoDto(store));
    }

    /**
     * 특정 위경도 범위 내 가게 조회
     *
     * @param geoLocationRange 위경도 범위 정보(최소 위도, 최대 위도, 최소 경도, 최대 경도)
     */
    @PostMapping("/search")
    public ResponseEntity<StoreSearchResultDto> getStores(
            @RequestBody GeoLocationRange geoLocationRange,
            @RequestBody FoodFilter foodFilter
    ) {
        List<Store> stores = storeService.getStores(geoLocationRange);
        List<Food> foods = new ArrayList<>();

        foodFilter.setStoreIds(stores.stream().map(Store::getStoreId).toList());
        StoreSearchResultDto storeSearchResultDto = new StoreSearchResultDto();

        boolean isFilterValid = foodFilter.getMenuCategoryId() != null || !foodFilter.getIngredientIds().isEmpty();
        if (!stores.isEmpty() && isFilterValid) {
            foods = foodService.filterFoods(foodFilter);

            Map<Long, Store> storeMap = stores.stream().collect(Collectors.toMap(Store::getStoreId, store -> store));
            stores = foods.stream().map(food -> storeMap.get(food.getStore().getStoreId())).toList();
        }
        storeSearchResultDto.setStoreInfos(stores.stream().map(storeMapper::toStoreInfoDto).toList());
        // TODO: 음식 정보를 추가해야 함
        // 음식 엔티티를 DTO로 변환하는 메서드는 FoodController에 존재 -> 컨트롤러간에 참조가 생기면 구조가 복잡해질 수 있음.
        return ResponseEntity.ok(storeSearchResultDto);
    }

    /**
     * OWNER 회원이 등록한 모든 가게 정보 조회
     */
    @CheckLogin
    @GetMapping("/my-stores")
    public ResponseEntity<List<StoreInfoDto>> getMyStores(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID, required = false) Long memberId) {
        List<Store> stores = storeService.getMyStores(memberId);
        List<StoreInfoDto> storeDtolist = stores.stream().map(storeMapper::toStoreInfoDto).toList();
        return ResponseEntity.ok(storeDtolist);
    }

    /**
     * OWNER 회원이 소유한 가게 정보 수정
     */
    @CheckLogin
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreInfoDto> updateStore(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID, required = false) Long memberId,
                                                    @PathVariable Long storeId,
                                                    @RequestBody StoreRequestDto storeRequestDto) {
        Store newStoreInfo = storeMapper.fromDto(storeRequestDto);
        Store newStore = storeService.updateStore(memberId, storeId, newStoreInfo);
        return ResponseEntity.ok(storeMapper.toStoreInfoDto(newStore));
    }

    /**
     * OWNER 회원이 소유한 가게 정보 수정 삭제
     */
    @CheckLogin
    @DeleteMapping("/{storeId}")
    public ResponseEntity<StoreInfoDto> deleteStore(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID, required = false) Long memberId,
                                                    @PathVariable Long storeId) {
        Store store = storeService.deleteStore(memberId, storeId);
        return ResponseEntity.ok(storeMapper.toStoreInfoDto(store));
    }
}
