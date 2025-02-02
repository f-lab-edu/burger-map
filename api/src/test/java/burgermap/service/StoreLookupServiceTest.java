package burgermap.service;

import burgermap.entity.Member;
import burgermap.entity.Store;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.exception.store.StoreNotExistException;
import burgermap.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreLookupServiceTest {
    @InjectMocks
    StoreLookupService storeLookupService;
    @Mock
    StoreRepository repository;

    @Test
    @DisplayName("존재하지 않는 가게 조회 시 StoreNotExistException 발생")
    void findByStoreId_whenFindUnregisteredStoreThrowException() {
        // given
        long unregisteredStoreId = -1L;
        when(repository.findByStoreId(unregisteredStoreId))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThrows(StoreNotExistException.class, () -> storeLookupService.findByStoreId(unregisteredStoreId));
    }

    @Test
    @DisplayName("존재하지 않는 가게의 존재 확인 시 StoreNotExistException 발생")
    void validateStoreExist_whenFindUnregisteredStoreThrowException() {
        // given
        long unregisteredStoreId = -1L;
        when(repository.existsByStoreId(unregisteredStoreId))
                .thenReturn(false);

        // when
        // then
        assertThrows(StoreNotExistException.class, () -> storeLookupService.validateStoreExists(unregisteredStoreId));
    }

    @Test
    @DisplayName("가게가 주어진 ID를 가진 회원의 소유가 아니면 NotOwnerMemberException 발생")
    void checkStoreBelongTo_verifyStoreIsOwnedByGivenMember() {
        // given
        long ownerId = 1L;
        Member owner = new Member();
        owner.setMemberId(ownerId);

        Store store = new Store();
        store.setMember(owner);

        long nonOwnerId = 99L;

        // when
        // then
        assertThrows(NotOwnerMemberException.class, () -> storeLookupService.checkStoreBelongTo(store, nonOwnerId));
    }
}
