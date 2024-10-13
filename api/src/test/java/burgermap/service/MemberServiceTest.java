package burgermap.service;

import burgermap.entity.Member;
import burgermap.enums.MemberType;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원 추가")
    void addMember() {
        // Member 객체를 레포지터리에 전달, 레포지터리에서 저장하며 Id 부여 -> Id 부여 확인
        Member newMember = new Member();
        newMember.setLoginId("testId");
        newMember.setPassword("testPw");
        newMember.setEmail("testId@gmail.com");
        newMember.setNickname("testNickname");
        newMember.setMemberType(MemberType.OWNER);

        Mockito.when(memberRepository.save(newMember)).thenAnswer(invocation ->
        {
            newMember.setMemberId(1L);
            return newMember;
        });

        memberService.addMember(newMember);

        assertThat(newMember.getMemberId()).isNotNull();
    }

    @Test
    @DisplayName("아이디 중복 확인 - 아이디가 중복되는 경우")
    void checkDuplicatedId() {
        // 아이디 중복되는 경우 레포지터리는 아이디를 가진 회원 객체(Optional<Member>) 반환
        // 서비스는 Optional.isPresent() 반환 -> true 반환 검증
        String duplicatedId = "duplicatedId";

        Mockito.when(memberRepository.findByLoginId(duplicatedId)).thenReturn(Optional.of(new Member()));
        boolean result = memberService.checkIdDuplication(duplicatedId);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("아이디 중복 확인 - 아이디가 중복되지 않는 경우")
    void checkNotDuplicatedId() {
        // 아이디 중복되지 않는 경우 레포지터리는 Optional.ofNullable(null) == Optional.empty() 반환
        // 서비스는 Optional.isPresent() 반환 -> false 반환 검증
        String notDuplicatedId = "notDuplicatedId";

        Mockito.when(memberRepository.findByLoginId(notDuplicatedId)).thenReturn(Optional.empty());
        boolean result = memberService.checkIdDuplication(notDuplicatedId);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("이메일 중복 확인 - 이메일이 중복되는 경우")
    void checkDuplicatedEmail() {
        String duplicatedEmail = "duplicatedEmail";

        Mockito.when(memberRepository.findByEmail(duplicatedEmail)).thenReturn(Optional.of(new Member()));
        boolean result = memberService.checkEmailDuplication(duplicatedEmail);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 확인 - 이메일이 중복되지 않는 경우")
    void checkNotDuplicatedEmail() {
        String notDuplicatedEmail = "notDuplicatedEmail";

        Mockito.when(memberRepository.findByEmail(notDuplicatedEmail)).thenReturn(Optional.empty());
        boolean result = memberService.checkEmailDuplication(notDuplicatedEmail);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 닉네임이 중복되는 경우")
    void checkDuplicatedNickname() {
        String duplicatedNickname = "duplicatedNickname";

        Mockito.when(memberRepository.findByNickname(duplicatedNickname)).thenReturn(Optional.of(new Member()));
        boolean result = memberService.checkNicknameDuplication(duplicatedNickname);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("닉네임 중복 확인 - 닉네임이 중복되지 않는 경우")
    void checkNotDuplicatedNickname() {
        String notDuplicatedNickname = "notDuplicatedNickname";

        Mockito.when(memberRepository.findByNickname(notDuplicatedNickname)).thenReturn(Optional.empty());
        boolean result = memberService.checkNicknameDuplication(notDuplicatedNickname);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("로그인 - 로그인 성공")
    void loginSuccess() {
        // 주어진 아이디를 가진 회원 조회 후 비밀번호 비교, 일치하면 회원 객체 반환 -> 반환된 회원 객체 검증
        String loginId = "testId";
        String password = "testPw";

        Member member = new Member();
        member.setLoginId(loginId);
        member.setPassword(password);

        Mockito.when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.of(member));
        Member result = memberService.login(loginId, password);

        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("로그인 - 로그인 실패 / 주어진 아이디를 가진 회원이 존재하지 않는 경우")
    void loginFailUnregisteredLoginId() {
        // 주어진 아이디를 가진 회원이 없다면 레포에서 Optional.empty() 반환 -> null 반환 검증
        String unregisteredLoginId = "unregisteredTestId";
        String password = "testPw";

        Mockito.when(memberRepository.findByLoginId(unregisteredLoginId)).thenReturn(Optional.empty());
        Member result = memberService.login(unregisteredLoginId, password);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("로그인 - 로그인 실패 / 비밀번호가 일치하지 않는 경우")
    void loginFailInvalidPassword() {
        // 주어진 아이디를 가진 회원 조회 후 비밀번호 비교 비밀번호가 일치하지 않는 경우 null 반환 -> 반환값 null 검증
        String loginId = "testId";
        String validPassword = "testPw";

        Member member = new Member();
        member.setLoginId(loginId);
        member.setPassword(validPassword);

        String invalidPassword = "invalidTestPw";

        Mockito.when(memberRepository.findByLoginId(loginId)).thenReturn(Optional.of(member));
        Member result = memberService.login(loginId, invalidPassword);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("내 정보 조회")
    void getMyInfo() {
        // 로그인한 회원 자신의 정보 조회 -> 회원 객체 반환 검증
        // 로그인 검증이 선행되므로 회원 조회는 반드시 성공한다고 가정.
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);

        Mockito.when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));
        Member result = memberService.getMyInfo(memberId);

        assertThat(result).isEqualTo(member);
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePassword() {
        // 레포에서 회원번호로 회원 조회 후 비밀번호 변경 -> 비밀번호 변경 여부 검증
        // 로그인 검증이 선행되므로 회원 조회는 반드시 성공한다고 가정.
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setPassword("oldPassword");
        String newPassword = "newPassword";

        Mockito.when(memberRepository.updatePassword(memberId, newPassword)).then(invocation -> {
            member.setPassword(newPassword);
            return Optional.of(member);
        });
        Member result = memberService.changePassword(memberId, newPassword);

        assertThat(result.getPassword()).isEqualTo(newPassword);
    }

    @Test
    @DisplayName("이메일 변경")
    void changeEmail() {
        // 레포에서 회원번호로 회원 조회 후 이메일 변경 -> 이메일 변경 여부 검증
        // 로그인 검증이 선행되므로 회원 조회는 반드시 성공한다고 가정.
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setEmail("old@gmail.com");
        String newEmail = "new@gmail.com";

        Mockito.when(memberRepository.updateEmail(memberId, newEmail)).then(invocation -> {
            member.setEmail(newEmail);
            return Optional.of(member);
        });
        Member result = memberService.changeEmail(memberId, newEmail);

        assertThat(result.getEmail()).isEqualTo(newEmail);
    }

    @Test
    @DisplayName("닉네임 변경")
    void changeNickname() {
        // 레포에서 회원번호로 회원 조회 후 닉네임 변경 -> 닉네임 변경 여부 검증
        // 로그인 검증이 선행되므로 회원 조회는 반드시 성공한다고 가정.
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setNickname("oldNickname");
        String newNickname = "newNickname";

        Mockito.when(memberRepository.updateNickname(memberId, newNickname)).then(invocation -> {
            member.setNickname(newNickname);
            return Optional.of(member);
        });
        Member result = memberService.changeNickname(memberId, newNickname);

        assertThat(result.getNickname()).isEqualTo(newNickname);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteMember() {
        // 레포에서 회원 번호로 회원 조회 후 제거, 조회된 회원 객체 반환 -> 회원 객체 반환 검증
        // 로그인 검증이 선행되므로 회원 조회는 반드시 성공한다고 가정.
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);

        Mockito.when(memberRepository.deleteByMemberId(memberId)).thenReturn(Optional.of(member));
        Member deletedMember = memberService.deleteMember(memberId);

        assertThat(deletedMember).isEqualTo(member);
    }

    @Test
    @DisplayName("회원 타입이 Owner인지 확인 - Owner인 경우")
    void isMemberTypeOwner() {
        // 레포에서 회원 번호로 회원 조회 후 타입 확인 -> Owner인 경우 예외가 발생하지 않음을 검증
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberType(MemberType.OWNER);

        Mockito.when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));

        assertThatCode(() -> memberService.isMemberTypeOwner(memberId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("회원 타입이 Owner인지 확인 - Owner가 아닌 경우")
    void isNotMemberTypeOwner() {
        // 레포에서 회원 번호로 회원 조회 후 타입 확인 -> Owner가 아닌 경우 NotOwnerMemberException 발생 검증
        Long memberId = 1L;
        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberType(MemberType.CUSTOMER);

        Mockito.when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.isMemberTypeOwner(memberId))
                .isInstanceOf(NotOwnerMemberException.class);
    }
}
