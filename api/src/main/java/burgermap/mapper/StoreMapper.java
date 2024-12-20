package burgermap.mapper;

import burgermap.dto.store.StoreInfoDto;
import burgermap.dto.store.StoreRequestDto;
import burgermap.entity.Store;
import org.springframework.stereotype.Component;

/**
 * Store 엔티티와 관련 DTO간의 양방향 맵핑을 제공하는 클래스.
 */
@Component
public class StoreMapper {
    public Store fromDto(StoreRequestDto storeRequestDto) {
        return Store.builder()
                .name(storeRequestDto.getName())
                .address(storeRequestDto.getAddress())
                .phone(storeRequestDto.getPhone())
                .introduction(storeRequestDto.getIntroduction())
                .build();
    }

    public StoreInfoDto toStoreInfoDto(Store store) {
        return StoreInfoDto.builder()
                .storeId(store.getStoreId())
                .name(store.getName())
                .address(store.getAddress())
                .phone(store.getPhone())
                .introduction(store.getIntroduction())
                .build();
    }
}
