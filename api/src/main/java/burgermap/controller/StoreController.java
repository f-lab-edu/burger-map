package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.store.StoreRequestDto;
import burgermap.dto.store.StoreInfoDto;
import burgermap.entity.Store;
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

    /**
     * 가게 추가
     */
    @CheckLogin
    @PostMapping
    public ResponseEntity<StoreInfoDto> addStore(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID, required = false) Long memberId,
                                                 @RequestBody StoreRequestDto storeRequestDto) {
        Store store = cvtToStore(storeRequestDto);
        storeService.addStore(store, memberId);
        System.out.println("store = " + store);
        return ResponseEntity.ok(cvtToStoreInfoDto(store));
    }

    /**
     * 특정 가게 조회
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreInfoDto> getStore(@PathVariable Long storeId) {
        Store store = storeService.getStore(storeId);
        return ResponseEntity.ok(cvtToStoreInfoDto(store));
    }

    /**
     * 조건을 만족하는 가게 조회
     *   - 주어진 위치에서 설정한 거리 내에 있는 가게 조회
     *   - 패티 종류, 번 종류에 따라 가게 조회
     *   - 사이드 메뉴 종류에 따라 가게 조회
     */
/*
    @GetMapping
    public ResponseEntity<List<StoreInfoDto>> getStores() {
        return ResponseEntity.ok(null);
    }
*/

    /**
     * OWNER 회원이 등록한 모든 가게 정보 조회
     */
    @CheckLogin
    @GetMapping("/my-stores")
    public ResponseEntity<List<StoreInfoDto>> getMyStores(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID, required = false) Long memberId) {
        List<Store> stores = storeService.getMyStores(memberId);
        List<StoreInfoDto> storeDtolist = stores.stream().map(store -> cvtToStoreInfoDto(store)).toList();
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
        Store newStoreInfo = cvtToStore(storeRequestDto);
        Store newStore = storeService.updateStore(memberId, storeId, newStoreInfo);
        return ResponseEntity.ok(cvtToStoreInfoDto(newStore));
    }

    /**
     * OWNER 회원이 소유한 가게 정보 수정 삭제
     */
    @CheckLogin
    @DeleteMapping("/{storeId}")
    public ResponseEntity<StoreInfoDto> deleteStore(@SessionAttribute(name = SessionConstants.LOGIN_MEMBER_ID, required = false) Long memberId,
                                                    @PathVariable Long storeId) {
        Store store = storeService.deleteStore(memberId, storeId);
        return ResponseEntity.ok(cvtToStoreInfoDto(store));
    }

    public Store cvtToStore(Object storeDto) {
        Store store = new Store();

        if (storeDto instanceof StoreRequestDto storeRequestDto) {
            store.setName(storeRequestDto.getName());
            store.setAddress(storeRequestDto.getAddress());
            store.setPhone(storeRequestDto.getPhone());
            store.setIntroduction(storeRequestDto.getIntroduction());
            // member 필드는 서비스 레이어에서 설정
        }

        return store;
    }

    public StoreInfoDto cvtToStoreInfoDto(Store store) {
        StoreInfoDto storeInfoDto = new StoreInfoDto();
        storeInfoDto.setStoreId(store.getStoreId());
        storeInfoDto.setName(store.getName());
        storeInfoDto.setAddress(store.getAddress());
        storeInfoDto.setPhone(store.getPhone());
        storeInfoDto.setIntroduction(store.getIntroduction());
        return storeInfoDto;
    }
}
