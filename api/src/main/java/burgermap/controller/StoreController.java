package burgermap.controller;

import burgermap.dto.store.StoreRequestDto;
import burgermap.dto.store.StoreInfoDto;
import burgermap.entity.Store;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.service.StoreService;
import burgermap.session.SessionConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("stores")
public class StoreController {

    private final StoreService storeService;

    /**
     * 가게 추가
     */
    @PostMapping
    public ResponseEntity<StoreInfoDto> addStore(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId,
                                                 @RequestBody StoreRequestDto storeRequestDto) {
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        System.out.println("storeAddRequestDto = " + storeRequestDto);
        Store store = cvtToStore(storeRequestDto, memberId);
        storeService.addStore(store);
        System.out.println("store = " + store);
        return ResponseEntity.ok(cvtToStoreInfoDto(store));
    }

    /**
     * 예외 처리: OWNER가 아닌 회원이 가게 추가 시도
     * 가게 추가 시, 멤버 타입이 OWNER가 아닌 경우 401 UNAUTHORIZED 반환
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotOwnerMemberException.class)
    public Map<String, String> handleNotOwnerMemberException(NotOwnerMemberException e) {
        return Map.of("message", e.getMessage());
    }

    /**
     * 특정 가게 조회
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreInfoDto> getStore(@PathVariable Long storeId) {
        Store store = storeService.getStore(storeId);
        if (store == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
    @GetMapping("/my-stores")
    public ResponseEntity<List<StoreInfoDto>> getMyStores(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId) {
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Store> stores = storeService.getMyStores(memberId);
        List<StoreInfoDto> storeDtolist = stores.stream().map(store -> cvtToStoreInfoDto(store)).toList();
        return ResponseEntity.ok(storeDtolist);
    }

    /**
     * OWNER 회원이 소유한 가게 정보 수정
     */
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreInfoDto> updateStore(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId,
                                                    @PathVariable Long storeId,
                                                    @RequestBody StoreRequestDto storeRequestDto) {
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Store newStoreInfo = cvtToStore(storeRequestDto, memberId);
        Store newStore = storeService.updateStore(memberId, storeId, newStoreInfo);

        if (newStore == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cvtToStoreInfoDto(newStore));
    }

    /**
     * OWNER 회원이 소유한 가게 정보 수정 삭제
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<StoreInfoDto> deleteStore(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId,
                                                    @PathVariable Long storeId) {
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Store store = storeService.deleteStore(memberId, storeId);
        if (store == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(cvtToStoreInfoDto(store));
    }

    public Store cvtToStore(Object storeDto, Long memberId) {
        Store store = new Store();

        if (storeDto instanceof StoreRequestDto storeRequestDto) {
            store.setName(storeRequestDto.getName());
            store.setAddress(storeRequestDto.getAddress());
            store.setPhone(storeRequestDto.getPhone());
            store.setIntroduction(storeRequestDto.getIntroduction());
            store.setMemberId(memberId);
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
