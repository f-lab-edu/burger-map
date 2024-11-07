package burgermap.service;

import burgermap.entity.Store;
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
}
