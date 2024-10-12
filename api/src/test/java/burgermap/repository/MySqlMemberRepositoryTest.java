package burgermap.repository;

import burgermap.entity.Member;
import burgermap.enums.MemberType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class MySqlMemberRepositoryTest extends TestcontainersTest{

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원 추가 및 회원 번호 조회")
    void saveAndFindByMemberId() {
        // 회원 추가 후 조회, 저장한 정보와 조회된 정보가 같은지 검증
        Member newMember = new Member();
        newMember.setLoginId("testId");
        newMember.setPassword("testPassword");
        newMember.setEmail("testEmail@gmail.com");
        newMember.setNickname("testNickname");
        newMember.setMemberType(MemberType.OWNER);

        memberRepository.save(newMember);
        Optional<Member> savedMember = memberRepository.findByMemberId(newMember.getMemberId());

        // hasValueSatisfying:
        // Verifies that the actual Optional contains a value and
        // gives this value to the given Consumer for further assertions.
        assertThat(savedMember).hasValueSatisfying(member -> {
            assertThat(member.getLoginId()).isEqualTo(newMember.getLoginId());
            assertThat(member.getPassword()).isEqualTo(newMember.getPassword());
            assertThat(member.getEmail()).isEqualTo(newMember.getEmail());
            assertThat(member.getNickname()).isEqualTo(newMember.getNickname());
            assertThat(member.getMemberType()).isEqualTo(newMember.getMemberType());
        });
    }

    @Test
    @DisplayName("존재하지 않는 회원 번호 조회")
    void findByUnregisteredMemberId() {
        // 존재하지 않는 회원 번호(-1L)로 조회, 결과: Optional.empty()
        Optional<Member> member = memberRepository.findByMemberId(-1L);

        assertThat(member).isEmpty();
    }

    @Test
    @DisplayName("로그인 아이디로 회원 조회")
    void findByLoginId() {
        // 회원 추가 후 로그인 아이디로 조회, 로그인 아이디 비교
        Member newMember = new Member();
        newMember.setLoginId("testId");

        memberRepository.save(newMember);
        Optional<Member> savedMember = memberRepository.findByMemberId(newMember.getMemberId());

        assertThat(savedMember).hasValueSatisfying(member -> {
            assertThat(member.getLoginId()).isEqualTo(newMember.getLoginId());
        });
    }

    @Test
    @DisplayName("존재하지 않는 로그인 아이디로 회원 조회")
    void findByUnregisteredLoginId() {
        // 존재하지 않는 로그인 아이디로 회원 조회, 결과: Optional.empty()
        Optional<Member> member = memberRepository.findByLoginId("unregisteredLoginId");

        assertThat(member).isEmpty();
    }

    @Test
    @DisplayName("이메일로 회원 조회")
    void findByEmail() {
        // 회원 추가 후 이메일로 조회, 이메일 비교
        Member newMember = new Member();
        newMember.setEmail("testEmail@gmail.com");

        memberRepository.save(newMember);
        Optional<Member> savedMember = memberRepository.findByMemberId(newMember.getMemberId());

        assertThat(savedMember).hasValueSatisfying(member -> {
            assertThat(member.getEmail()).isEqualTo(newMember.getEmail());
        });
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원 조회")
    void findByUnregisteredEmail() {
        // 존재하지 않는 이메일로 회원 조회, 결과: Optional.empty()
        Optional<Member> member = memberRepository.findByEmail("unregisteredEmail@gmail.com");

        assertThat(member).isEmpty();
    }

    @Test
    @DisplayName("닉네임으로 회원 조회")
    void findByNickname() {
        // 회원 추가 후 닉네임으로 조회, 닉네임 비교
        Member newMember = new Member();
        newMember.setNickname("testNickname");

        memberRepository.save(newMember);
        Optional<Member> savedMember = memberRepository.findByMemberId(newMember.getMemberId());

        assertThat(savedMember).hasValueSatisfying(member -> {
            assertThat(member.getNickname()).isEqualTo(newMember.getNickname());
        });
    }

    @Test
    @DisplayName("존재하지 않는 닉네임으로 회원 조회")
    void findByUnregisteredNickname() {
        // 존재하지 않는 닉네임으로 회원 조회, 결과: Optional.empty()
        Optional<Member> member = memberRepository.findByNickname("unregisteredNickname");

        assertThat(member).isEmpty();
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteByMemberId() {
        // 회원 추가 후 삭제, 삭제된 회원을 조회시 Optional.empty() 반환 검증
        Member newMember = new Member();
        newMember.setLoginId("testId");
        newMember.setPassword("testPassword");
        newMember.setEmail("testEmail@gmail.com");
        newMember.setNickname("testNickname");
        newMember.setMemberType(MemberType.OWNER);
        memberRepository.save(newMember);

        memberRepository.deleteByMemberId(newMember.getMemberId());
        Optional<Member> deletedMember = memberRepository.findByMemberId(newMember.getMemberId());

        assertThat(deletedMember).isEmpty();
    }
}
