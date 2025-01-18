package burgermap.repository;

import burgermap.entity.Member;
import burgermap.entity.Store;
import burgermap.enums.MemberType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Import(MySqlStoreRepository.class)
class MySqlStoreRepositoryTest extends TestcontainersTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("가게 추가 및 가게 번호 조회")
    void saveAndFindByStoreId() {
        // 가게 추가 후 조회, 저장한 정보와 조회된 정보가 같은지 검증
        Member member = new Member();
        member.setMemberType(MemberType.OWNER);
        entityManager.persist(member);

        Store newStore = new Store();
        newStore.setMember(member);
        newStore.setName("testStore");
        newStore.setPhone("010-1234-5678");
        newStore.setAddress("testAddress");
        newStore.setIntroduction("testIntroduction");

        storeRepository.save(newStore);
        Optional<Store> savedStore = storeRepository.findByStoreId(newStore.getStoreId());

        assertThat(savedStore).hasValueSatisfying(store -> {
            assertThat(store.getMember().getMemberId()).isEqualTo(member.getMemberId());
            assertThat(store.getName()).isEqualTo(newStore.getName());
            assertThat(store.getPhone()).isEqualTo(newStore.getPhone());
            assertThat(store.getAddress()).isEqualTo(newStore.getAddress());
            assertThat(store.getIntroduction()).isEqualTo(newStore.getIntroduction());
        });
    }

    @Test
    @DisplayName("존재하지 않는 가게 번호 조회")
    void findByUnregisteredStoreId() {
        // 존재하지 않는 가게 번호(-1L)로 조회, 결과: Optional.empty()
        Member member = new Member();
        member.setMemberType(MemberType.OWNER);
        entityManager.persist(member);

        Store newStore = new Store();
        newStore.setMember(member);
        newStore.setName("testStore");
        newStore.setPhone("010-1234-5678");
        newStore.setAddress("testAddress");
        newStore.setIntroduction("testIntroduction");
        storeRepository.save(newStore);

        Optional<Store> store = storeRepository.findByStoreId(-1L);

        assertThat(store).isEmpty();
    }

    @Test
    @DisplayName("가게를 소유한 회원 번호로 가게 조회")
    void findByMemberId() {
        // 가게 정보에 포함된 회원의 번호로 가게 조회
        Member member = new Member();
        member.setMemberType(MemberType.OWNER);
        entityManager.persist(member);

        Store newStore1 = new Store();
        newStore1.setMember(member);
        newStore1.setName("testStore1");
        newStore1.setPhone("010-1234-1111");
        newStore1.setAddress("testAddress1");
        newStore1.setIntroduction("testIntroduction1");

        Store newStore2 = new Store();
        newStore2.setMember(member);
        newStore2.setName("testStore2");
        newStore2.setPhone("010-1234-2222");
        newStore2.setAddress("testAddress2");
        newStore2.setIntroduction("testIntroduction2");

        storeRepository.save(newStore1);
        storeRepository.save(newStore2);
        List<Store> stores = storeRepository.findByMemberId(member.getMemberId());

        assertThat(stores).hasSize(2);
        // Extract the values of the given field or property from the Iterable's elements
        // under test into a new Iterable, this new Iterable becoming the Iterable under test.
        assertThat(stores).extracting("name")
                .containsExactly(newStore1.getName(), newStore2.getName());
        assertThat(stores).extracting("phone")
                .containsExactly(newStore1.getPhone(), newStore2.getPhone());
        assertThat(stores).extracting("address")
                .containsExactly(newStore1.getAddress(), newStore2.getAddress());
        assertThat(stores).extracting("introduction")
                .containsExactly(newStore1.getIntroduction(), newStore2.getIntroduction());
    }

    @Test
    @DisplayName("가게를 소유하지 않은 회원 번호로 가게 조회")
    void findByNotOwnerMemberId() {
        // 가게를 등록하지 않은 회원 번호(-1L)로 가게 조회, 결과: empty list
        Member member = new Member();
        member.setMemberType(MemberType.OWNER);
        entityManager.persist(member);

        Store newStore1 = new Store();
        newStore1.setMember(member);
        newStore1.setName("testStore1");
        newStore1.setPhone("010-1234-1111");
        newStore1.setAddress("testAddress1");
        newStore1.setIntroduction("testIntroduction1");

        Store newStore2 = new Store();
        newStore1.setMember(member);
        newStore1.setName("testStore2");
        newStore1.setPhone("010-1234-2222");
        newStore1.setAddress("testAddress2");
        newStore1.setIntroduction("testIntroduction2");

        storeRepository.save(newStore1);
        storeRepository.save(newStore2);

        List<Store> stores = storeRepository.findByMemberId(-1L);

        assertThat(stores).isEmpty();
    }

    @Test
    @DisplayName("가게 삭제")
    void deleteByStoreId() {
        // 가게 등록 후 삭제, 삭제된 가게 정보가 조회시 Optional.empty() 반환 검증
        Member member = new Member();
        member.setMemberType(MemberType.OWNER);
        entityManager.persist(member);

        Store newStore = new Store();
        newStore.setMember(member);
        newStore.setName("testStore");
        newStore.setPhone("010-1234-5678");
        newStore.setAddress("testAddress");
        newStore.setIntroduction("testIntroduction");

        storeRepository.save(newStore);
        storeRepository.deleteByStoreId(newStore.getStoreId());

        Optional<Store> store = storeRepository.findByStoreId(newStore.getStoreId());
        assertThat(store).isEmpty();
    }
}
