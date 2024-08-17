package burgermap.repository;


import burgermap.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MySqlMemberRepositoryTest {

    @Autowired
    MySqlMemberRepository memberRepository;

    /**
     * 테스트용 회원 데이터 추가
     */
    HashMap<Long, Member> addNMembers(int numMembers) {
        HashMap<Long, Member> members = new HashMap<>();

        for (long i = 1; i <= numMembers; i++) {
            Member member = new Member();
            member.setLoginId("testId" + i);
            member.setPassword("testPw" + i);
            member.setEmail("test" + i + "@gmail.com");

            memberRepository.addMember(member);
            members.put(member.getMemberId(), member);
        }

        return members;
    }

    @Test
    @DisplayName("회원 추가")
    void addMemberTest() {
        // given
        Member member1 = new Member(null, "testId1", "testPw1", "test1@gmail.com");
        Member member2 = new Member(null, "testId2", "testPw2", "test2@gmail.com");
        memberRepository.addMember(member1);
        memberRepository.addMember(member2);

        // when
        Member foundMember1 = memberRepository.findMember(member1.getMemberId());
        Member foundMember2 = memberRepository.findMember(member2.getMemberId());

        // then
        assertThat(foundMember1).isEqualTo(member1);
        assertThat(foundMember2).isEqualTo(member2);
    }

    @Test
    @DisplayName("회원 조회")
    void findMemberTest() {
        // given
        HashMap<Long, Member> memberHashMap = addNMembers(5);

        // when
        ArrayList<Long> ids = new ArrayList<>(memberHashMap.keySet());
        Member savedMember1 = memberHashMap.get(ids.get(0));
        Member savedMember2 = memberHashMap.get(ids.get(1));

        Member foundMember1 = memberRepository.findMember(savedMember1.getMemberId());
        Member foundMember2 = memberRepository.findMember(savedMember2.getMemberId());

        // then
        assertThat(foundMember1).isEqualTo(savedMember1);
        assertThat(foundMember2).isEqualTo(savedMember2);
    }

    @Test
    @DisplayName("미등록 회원 조회")
    void findUnregisteredMember() {
        // given
        HashMap<Long, Member> memberHashMap = addNMembers(5);

        // when
        ArrayList<Long> ids = new ArrayList<>(memberHashMap.keySet());
        long notExistId = ids.stream().mapToLong(l -> l).sum();
        Member member = memberRepository.findMember(notExistId);

        // then
        assertThat(member).isNull();
    }

    @Test
    @DisplayName("모든 회원 조회")
    void findAllMembersTest() {
        // given
        HashMap<Long, Member> memberHashMap = addNMembers(5);

        // when
        List<Member> allMembers = memberRepository.findAllMembers();

        // then
        assertThat(allMembers).containsOnlyOnceElementsOf(memberHashMap.values());
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMemberTest() {
        // given
        HashMap<Long, Member> memberHashMap = addNMembers(5);

        // when
        ArrayList<Long> ids = new ArrayList<>(memberHashMap.keySet());
        Member deletedMember1 = memberRepository.deleteMember(ids.get(0));
        Member deletedMember2 = memberRepository.deleteMember(ids.get(1));
        Member foundDeletedMember1 = memberRepository.findMember(deletedMember1.getMemberId());
        Member foundDeletedMember2 = memberRepository.findMember(deletedMember2.getMemberId());

        // then
        assertThat(deletedMember1).isEqualTo(memberHashMap.get(deletedMember1.getMemberId()));
        assertThat(foundDeletedMember1).isNull();
        assertThat(deletedMember2).isEqualTo(memberHashMap.get(deletedMember2.getMemberId()));
        assertThat(foundDeletedMember2).isNull();
    }

    @Test
    @DisplayName("미등록 회원 삭제")
    void deleteUnregisteredMemberTest() {
        // given
        HashMap<Long, Member> memberHashMap = addNMembers(5);

        // when
        ArrayList<Long> ids = new ArrayList<>(memberHashMap.keySet());
        long notExistId = ids.stream().mapToLong(l -> l).sum();
        Member notExistMember = memberRepository.findMember(notExistId);
        Member deletedMember = memberRepository.deleteMember(notExistId);

        // then
        assertThat(notExistMember).isNull();
        assertThat(deletedMember).isNull();
    }
}
