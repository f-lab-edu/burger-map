package burgermap.repository;

import burgermap.entity.Store;

import java.util.List;
import java.util.Optional;

public interface StoreRepository {
    public Store save(Store store);
    public Optional<Store> findByStoreId(Long storeId);
    public List<Store> findByMemberId(Long memberId);
    public Optional<Store> deleteByStoreId(Long storeId);

    public boolean existsByStoreId(Long storeId);
    public List<Store> findByGeoRange(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);
}
