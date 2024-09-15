package burgermap.service;

import burgermap.entity.Member;
import burgermap.entity.Store;
import burgermap.enums.MemberType;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.exception.store.StoreNotExistException;
import burgermap.repository.MemberRepository;
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

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public void addStore(Store store) {
        Member member = memberRepository.findByMemberId(store.getMemberId()).get();
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }

        storeRepository.save(store);
        log.debug("store added: {}", store);
    }

    public Store getStore(Long storeId) {
        Store store = storeRepository.findByStoreId(storeId).orElse(null);
        log.debug("store info: {}", store);
        return store;
    }

    public List<Store> getMyStores(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId).get();
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }

        List<Store> stores = storeRepository.findByMemberId(memberId);
        log.debug("member {} - stores: {}", memberId, stores);
        return stores;
    }

    public Store updateStore(Long requestMemberId, Long storeId, Store newStoreInfo) {
        Member member = memberRepository.findByMemberId(requestMemberId).get();
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }

        Store oldStore = storeRepository.findByStoreId(storeId).orElse(null);
        if (oldStore == null) {  // 존재하지 않는 가게
            throw new StoreNotExistException(storeId);
        }
        else if (!oldStore.getMemberId().equals(requestMemberId)) {  // 요청자가 가게의 소유자가 아님
            throw new NotOwnerMemberException("member is not owner of the store.");
        }

        Optional<Store> newStore = storeRepository.updateStore(storeId, newStoreInfo);
        return newStore.orElse(null);
    }

    public Store deleteStore(Long requestMemberId, Long storeId) {
        Member member = memberRepository.findByMemberId(requestMemberId).get();
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }

        Store oldStore = storeRepository.findByStoreId(storeId).orElse(null);
        if (oldStore == null) {  // 존재하지 않는 가게
            throw new StoreNotExistException(storeId);
        }
        else if (!oldStore.getMemberId().equals(requestMemberId)) {  // 요청자가 가게의 소유자가 아님
            throw new NotOwnerMemberException("member is not owner of the store.");
        }

        Optional<Store> deletedStore = storeRepository.deleteByStoreId(storeId);
        return deletedStore.orElse(null);
    }
}
