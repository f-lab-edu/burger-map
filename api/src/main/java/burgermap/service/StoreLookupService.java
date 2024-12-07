package burgermap.service;

import burgermap.entity.Store;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.exception.store.StoreNotExistException;
import burgermap.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreLookupService {

    private final StoreRepository repository;

    Store findByStoreId(Long storeId) {
        return repository.findByStoreId(storeId)
                .orElseThrow(() -> new StoreNotExistException(storeId));
    }

    /**
     * store가 memberId의 소유가 아니면 NotOwnerMemberException을 발생시킴
     */
    public void checkStoreBelongTo(Store store, long memberId) {
        if (store.getMember().getMemberId() != memberId) {
            throw new NotOwnerMemberException("member is not owner of the store.");
        }
    }

    /**
     * storeId에 해당하는 가게가 존재하지 않으면 StoreNotExistException 발생
     */
    public void validateStoreExists(Long storeId) {
        if (!repository.existsByStoreId(storeId)) {
            throw new StoreNotExistException(storeId);
        }
    }
}
