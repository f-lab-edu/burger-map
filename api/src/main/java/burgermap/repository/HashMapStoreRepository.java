package burgermap.repository;

import burgermap.entity.Store;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class HashMapStoreRepository implements StoreRepository{

    private final Map<Long, Store> repository = new ConcurrentHashMap<>();
    private final AtomicLong storeIdCount = new AtomicLong(0);

    @Override
    public Store save(Store store) {
        store.setStoreId(storeIdCount.incrementAndGet());
        repository.put(store.getStoreId(), store);
        return store;
    }

    @Override
    public Optional<Store> findByStoreId(Long storeId) {
        return Optional.ofNullable(repository.get(storeId));
    }

    @Override
    public List<Store> findByMemberId(Long memberId) {
        List<Store> stores = repository.values().stream()
                .filter(store -> store.getMemberId().equals(memberId))
                .toList();
        return stores;
    }

    @Override
    public Optional<Store> updateStore(Long storeId, Store newStore) {
        Store oldStore = repository.get(storeId);
        if (oldStore == null) {
            return Optional.empty();
        }

        newStore.setStoreId(storeId);
        repository.put(storeId, newStore);
        return Optional.of(newStore);
    }

    @Override
    public Optional<Store> deleteByStoreId(Long storeId) {
        Store store = repository.remove(storeId);
        return Optional.ofNullable(store);
    }
}
