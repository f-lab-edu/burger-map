package burgermap.repository;

import burgermap.entity.Store;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
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
                .filter(store -> store.getMember().getMemberId().equals(memberId))
                .toList();
        return stores;
    }

    @Override
    public Optional<Store> deleteByStoreId(Long storeId) {
        Store store = repository.remove(storeId);
        return Optional.ofNullable(store);
    }
}
