package burgermap.service;

import burgermap.entity.Store;
import burgermap.exception.member.MemberNotExistException;
import burgermap.exception.store.StoreNotExistException;
import burgermap.repository.MemberRepository;
import burgermap.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StoreService {
    private final MemberLookupService memberLookupService;
    private final StoreLookupService storeLookupService;

    private final StoreRepository storeRepository;

    public void addStore(Store store, Long memberId) {
        memberLookupService.isMemberTypeOwner(memberId);
        store.setMember(memberLookupService.findByMemberId(memberId));
        storeRepository.save(store);
        log.debug("store added: {}", store);
    }

    public Store getStore(Long storeId) {
        Store store = storeLookupService.findByStoreId(storeId);
        log.debug("store info: {}", store);
        return store;
    }

    public List<Store> getMyStores(Long memberId) {
        memberLookupService.isMemberTypeOwner(memberId);

        List<Store> stores = storeRepository.findByMemberId(memberId);
        log.debug("member {} - stores: {}", memberId, stores);
        return stores;
    }

    public Store updateStore(Long requestMemberId, Long storeId, Store newStoreInfo) {
        memberLookupService.isMemberTypeOwner(requestMemberId);
        Store store = checkStoreExistence(storeId);
        storeLookupService.checkStoreBelongTo(store, requestMemberId);

        store.setName(newStoreInfo.getName());
        store.setAddress(newStoreInfo.getAddress());
        store.setPhone(newStoreInfo.getPhone());
        store.setIntroduction(newStoreInfo.getIntroduction());
        return store;
    }

    public Store deleteStore(Long requestMemberId, Long storeId) {
        memberLookupService.isMemberTypeOwner(requestMemberId);
        Store store = checkStoreExistence(storeId);
        storeLookupService.checkStoreBelongTo(store, requestMemberId);

        storeRepository.deleteByStoreId(storeId);
        log.debug("store deleted: {}", store);
        return store;
    }

    /**
     * storeId에 해당하는 가게가 존재하지 않으면 StoreNotExistException을 발생시킴
     */
    public Store checkStoreExistence(Long storeId) {
        return storeRepository.findByStoreId(storeId)
                .orElseThrow(() -> new StoreNotExistException(storeId));
    }
}
