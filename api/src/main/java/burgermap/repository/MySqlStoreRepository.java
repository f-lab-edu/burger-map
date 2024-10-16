package burgermap.repository;

import burgermap.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MySqlStoreRepository implements StoreRepository {

    private final SpringDataJpaStoreRepository repository;

    @Override
    public Store save(Store store) {
        repository.save(store);
        return store;
    }

    @Override
    public Optional<Store> findByStoreId(Long storeId) {
        return repository.findById(storeId);
    }

    @Override
    public List<Store> findByMemberId(Long memberId) {
        return repository.findByMemberId(memberId);
    }

    @Override
    public Optional<Store> deleteByStoreId(Long storeId) {
        Store store = repository.findById(storeId).get();
        repository.delete(store);
        return Optional.of(store);
    }
}
