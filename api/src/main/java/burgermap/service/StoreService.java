package burgermap.service;

import burgermap.dto.geo.GeoLocation;
import burgermap.dto.geo.GeoLocationRange;
import burgermap.entity.Member;
import burgermap.entity.Store;
import burgermap.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {
    private final MemberLookupService memberLookupService;
    private final StoreLookupService storeLookupService;
    private final GeoCodingService geoCodingService;

    private final StoreRepository storeRepository;

    @Transactional
    public void addStore(Store store, Long memberId) {
        Member member = memberLookupService.isMemberTypeOwner(memberId);
        Optional<GeoLocation> geoLocation = geoCodingService.getGeoLocation(store.getAddress());

        store.setMember(member);
        geoLocation.ifPresent(location -> {
            store.setLatitude(location.getLatitude());
            store.setLongitude(location.getLongitude());
        });
        storeRepository.save(store);
        log.debug("store added: {}", store);
    }

    public Store getStore(Long storeId) {
        Store store = storeLookupService.findByStoreId(storeId);
        log.debug("store info: {}", store);
        return store;
    }

    public List<Store> getStores(GeoLocationRange geoLocationRange) {
        return storeRepository.findByGeoRange(
                geoLocationRange.getMinLatitude(),
                geoLocationRange.getMaxLatitude(),
                geoLocationRange.getMinLongitude(),
                geoLocationRange.getMaxLongitude()
        );
    }

    public List<Store> getMyStores(Long memberId) {
        memberLookupService.isMemberTypeOwner(memberId);

        List<Store> stores = storeRepository.findByMemberId(memberId);
        log.debug("member {} - stores: {}", memberId, stores);
        return stores;
    }

    @Transactional
    public Store updateStore(Long requestMemberId, Long storeId, Store newStoreInfo) {
        memberLookupService.isMemberTypeOwner(requestMemberId);
        Store store = storeLookupService.findByStoreId(storeId);
        storeLookupService.checkStoreBelongTo(store, requestMemberId);

        store.setName(newStoreInfo.getName());
        store.setAddress(newStoreInfo.getAddress());
        store.setPhone(newStoreInfo.getPhone());
        store.setIntroduction(newStoreInfo.getIntroduction());
        return store;
    }

    @Transactional
    public Store deleteStore(Long requestMemberId, Long storeId) {
        memberLookupService.isMemberTypeOwner(requestMemberId);
        Store store = storeLookupService.findByStoreId(storeId);
        storeLookupService.checkStoreBelongTo(store, requestMemberId);

        storeRepository.deleteByStoreId(storeId);
        log.debug("store deleted: {}", store);
        return store;
    }
}
