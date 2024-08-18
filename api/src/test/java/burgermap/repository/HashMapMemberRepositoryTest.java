package burgermap.repository;

import burgermap.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HashMapMemberRepositoryTest {
    HashMapMemberRepository repository = new HashMapMemberRepository();

    @AfterEach
    void clearRepository() {
        repository.clear();
    }

    @Test
    @DisplayName("회원 추가 및 조회")
    void addAndFindMember() {
        // given
        Member member1 = new Member(1L, "testId1", "test1", "test1@gmail.com");
        Member member2 = new Member(2L, "testId2", "test2", "test2@gmail.com");
        repository.addMember(member1);
        repository.addMember(member2);

        // when
        Member foundMember1 = repository.findMember(1L);
        Member foundMember2 = repository.findMember(2L);

        // then
        assertThat(foundMember1).isEqualTo(member1);
        assertThat(foundMember2).isEqualTo(member2);
    }

    @Test
    @DisplayName("미등록 회원 조회")
    void findUnregisteredMember() {
        // given
        Member member1 = new Member(1L, "testId1", "test1", "test1@gmail.com");
        Member member2 = new Member(2L, "testId2", "test2", "test2@gmail.com");
        repository.addMember(member1);
        repository.addMember(member2);

        // when
        Member unregisteredMember = repository.findMember(5L);

        // then
        assertThat(unregisteredMember).isNull();
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMember() {
        // given
        Member member1 = new Member(1L, "testId1", "test1", "test1@gmail.com");
        Member member2 = new Member(2L, "testId2", "test2", "test2@gmail.com");
        repository.addMember(member1);
        repository.addMember(member2);

        // when - 회원을 삭제하고 조회
        Long deleteTargetMemberId = member2.getMemberId();
        Member deletedMember = repository.deleteMember(deleteTargetMemberId);
        Member foundDeletedMember = repository.findMember(deleteTargetMemberId);

        // then
        assertThat(deletedMember).isEqualTo(member2);
        assertThat(foundDeletedMember).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 회원 삭제")
    void deleteUnregisteredMember() {
        // given
        Member member1 = new Member(1L, "testId1", "test1", "test1@gmail.com");
        Member member2 = new Member(2L, "testId2", "test2", "test2@gmail.com");
        repository.addMember(member1);
        repository.addMember(member2);

        // when
        Member deletedMember = repository.deleteMember(-1L);

        // then
        assertThat(deletedMember).isNull();
    }
}
