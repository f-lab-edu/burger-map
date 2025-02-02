package burgermap.service;

import burgermap.dto.geo.GeoLocation;
import burgermap.entity.Member;
import burgermap.entity.Store;
import burgermap.enums.MemberType;
import burgermap.repository.StoreRepository;
import burgermap.service.geoCoding.GeoCodingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    StoreService storeService;

    @Mock
    MemberLookupService memberLookupService;
    @Mock
    StoreLookupService storeLookupService;
    @Mock
    GeoCodingService geoCodingService;
    @Mock
    StoreRepository storeRepository;

    @Test
    @DisplayName("가게 등록 시 가게 객체에 회원 객체와 위경도 정보를 주입함.")
    void addStore_injectsOwnerAndGeoLocation () {
        // given
        Store store = new Store();
        // 가게 등록 회원
        long memberId = 1L;
        Member storeOwner = new Member();

        // 가게 위치 정보
        GeoLocation geoLocation = new GeoLocation(123.456, 654.321);

        when(memberLookupService.isMemberTypeOwner(memberId))
                .thenReturn(storeOwner);
        when(geoCodingService.getGeoLocation(store.getAddress()))
                .thenReturn(Optional.of(geoLocation));
        when(storeRepository.save(store)).thenReturn(store);

        // when
        storeService.addStore(store, memberId);

        // then
        // 가게 객체에 회원 객체와 위경도 정보가 주입되었는지 검증
        assertThat(store.getMember()).isEqualTo(storeOwner);
        assertThat(store.getLatitude()).isEqualTo(geoLocation.getLatitude());
        assertThat(store.getLongitude()).isEqualTo(geoLocation.getLongitude());
    }

    @Test
    @DisplayName("가게 정보 수정 시 기존 가게의 이름, 주소, 전화번호, 소개를 새로운 정보로 갱신함.")
    void updateStore_updatesStoreNameAddressPhoneAndIntroduction() {
        // given
        long requestMemberId = 1L;
        // 기존 정보를 담은 가게 객체
        Store store = new Store();
        store.setStoreId(1L);
        store.setName("oldName");
        store.setAddress("oldAddress");
        store.setPhone("02-0000-0000");
        store.setIntroduction("oldIntroduction");
        // 새로운 정보를 담은 가게 객체
        Store newStoreInfo = new Store();
        newStoreInfo.setName("newName");
        newStoreInfo.setAddress("newAddress");
        newStoreInfo.setPhone("02-1111-1111");
        newStoreInfo.setIntroduction("newIntroduction");

        when(memberLookupService.isMemberTypeOwner(requestMemberId))
                .thenReturn(null);
        when(storeLookupService.findByStoreId(store.getStoreId()))
                .thenReturn(store);
        doNothing()
                .when(storeLookupService).checkStoreBelongTo(store, requestMemberId);

        // when
        storeService.updateStore(requestMemberId, store.getStoreId(), newStoreInfo);

        // then
        // 가게 객체의 이름, 주소, 전화번호, 소개를 새로운 정보로 갱신하였는지 검증
        assertThat(store.getName()).isEqualTo(newStoreInfo.getName());
        assertThat(store.getAddress()).isEqualTo(newStoreInfo.getAddress());
        assertThat(store.getPhone()).isEqualTo(newStoreInfo.getPhone());
        assertThat(store.getIntroduction()).isEqualTo(newStoreInfo.getIntroduction());
    }

    @Test
    @DisplayName("가게 삭제 시 요청자의 타입, 요청자가 소유주인지 확인함.")
    void deleteStore_verifyRequesterTypeAndRequesterIsStoreOwner() {
        // given
        // 가게와 소유주
        long requesterMemberId = 1L;
        Member storeOwner = new Member(
                requesterMemberId, MemberType.OWNER, null, null, null, null, null);

        long storeId = 1L;
        Store store = new Store();
        store.setStoreId(storeId);
        store.setMember(storeOwner);

        when(memberLookupService.isMemberTypeOwner(requesterMemberId))
                .thenReturn(storeOwner);
        when(storeLookupService.findByStoreId(storeId))
                .thenReturn(store);
        doNothing()
                .when(storeLookupService).checkStoreBelongTo(store, requesterMemberId);
        when(storeRepository.deleteByStoreId(storeId)).
                thenReturn(Optional.of(store));

        // when
        storeService.deleteStore(requesterMemberId, storeId);

        // then
        // 삭제 요청자에 대한 검증 절차가 호출되었는지 검증.
        // 고민: 분기 없는 단순 메서드 호출을 수행하는 메서드도  행위 기반의 테스트가 필요한지?
        verify(memberLookupService).isMemberTypeOwner(requesterMemberId);
        verify(storeLookupService).findByStoreId(storeId);
        verify(storeLookupService).checkStoreBelongTo(store, requesterMemberId);
        verify(storeRepository).deleteByStoreId(storeId);
        verifyNoMoreInteractions(memberLookupService, storeLookupService, storeRepository);
    }
}
