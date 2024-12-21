package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.store.StoreInfoDto;
import burgermap.dto.store.StoreRequestDto;
import burgermap.dto.store.StoreSearchRequestDto;
import burgermap.dto.store.StoreSearchResponseDto;
import burgermap.dto.store.StoresWithFoodsEntityWrapper;
import burgermap.entity.Store;
import burgermap.mapper.StoreMapper;
import burgermap.service.SearchService;
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

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("stores")
public class StoreController {

    private final StoreService storeService;
    private final SearchService searchService;

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
     * @param storeSearchRequestDto 위경도 범위와 음식 필터 정보를 포함하는 객체
     */
    @PostMapping("/search")
    public ResponseEntity<StoreSearchResponseDto> getStores(@RequestBody StoreSearchRequestDto storeSearchRequestDto) {
        StoresWithFoodsEntityWrapper storesWithFoodsEntityWrapper = searchService.searchStores(
                storeSearchRequestDto.getGeoLocationRange(),
                storeSearchRequestDto.getFoodFilter());
        return ResponseEntity.ok(storeMapper.toStoreSearchResultDto(storesWithFoodsEntityWrapper));
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
