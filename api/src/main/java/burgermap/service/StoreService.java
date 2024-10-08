package burgermap.service;

import burgermap.entity.Store;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.exception.store.StoreNotExistException;
import burgermap.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final MemberService memberService;

    private final StoreRepository storeRepository;

    public void addStore(Store store) {
        memberService.isMemberTypeOwner(store.getMemberId());

        storeRepository.save(store);
        log.debug("store added: {}", store);
    }

    public Store getStore(Long storeId) {
        Store store = storeRepository.findByStoreId(storeId).orElseThrow(() -> new StoreNotExistException(storeId));
        log.debug("store info: {}", store);
        return store;
    }

    public List<Store> getMyStores(Long memberId) {
        memberService.isMemberTypeOwner(memberId);

        List<Store> stores = storeRepository.findByMemberId(memberId);
        log.debug("member {} - stores: {}", memberId, stores);
        return stores;
    }

    public Store updateStore(Long requestMemberId, Long storeId, Store newStoreInfo) {
        memberService.isMemberTypeOwner(requestMemberId);
        Store oldStore = checkStoreExistence(storeId);
        checkStoreBelongTo(oldStore, requestMemberId);

        Optional<Store> newStore = storeRepository.updateStore(storeId, newStoreInfo);
        return newStore.orElse(null);
    }

    public Store deleteStore(Long requestMemberId, Long storeId) {
        memberService.isMemberTypeOwner(requestMemberId);
        Store oldStore = checkStoreExistence(storeId);
        checkStoreBelongTo(oldStore, requestMemberId);

        Optional<Store> deletedStore = storeRepository.deleteByStoreId(storeId);
        return deletedStore.orElse(null);
    }

    /**
     * storeId에 해당하는 가게가 존재하지 않으면 StoreNotExistException을 발생시킴
     */
    public Store checkStoreExistence(Long storeId) {
        Store store = storeRepository.findByStoreId(storeId).orElse(null);
        if (store == null) {
            throw new StoreNotExistException(storeId);
        }
        return store;
    }

    /**
     * store가 memberId의 소유가 아니면 NotOwnerMemberException을 발생시킴
     */
    public void checkStoreBelongTo(Store store, Long memberId) {
        if (!store.getMemberId().equals(memberId)) {
            throw new NotOwnerMemberException("member is not owner of the store.");
        }
    }
}
