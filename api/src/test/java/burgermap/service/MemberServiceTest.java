package burgermap.service;

import burgermap.dto.member.MemberJoinDto;
import burgermap.dto.member.MemberResponseDto;
import burgermap.entity.Member;
import burgermap.repository.HashMapMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    HashMapMemberRepository memberRepository;

    @Test
    @DisplayName("회원 추가")
    void addMember() {
        // given
        MemberJoinDto memberJoinDto = new MemberJoinDto();
        memberJoinDto.setLoginId("testId");
        memberJoinDto.setPassword("testPw");
        memberJoinDto.setEmail("test@gmail.com");

        given(memberRepository.addMember(any(Member.class))).willReturn(null);

        // when
        MemberResponseDto memberResponseDto = memberService.addMember(memberJoinDto);

        // then
        assertThat(memberResponseDto.getLoginId()).isEqualTo(memberJoinDto.getLoginId());
        assertThat(memberResponseDto.getEmail()).isEqualTo(memberJoinDto.getEmail());
    }

    @Test
    @DisplayName("회원 조회")
    void findMemberByMemberId() {
        // given
        Member member = new Member();
        member.setMemberId(99L);
        member.setLoginId("testId99");
        member.setPassword("testPw99");
        member.setEmail("test99@gmail.com");

        given(memberRepository.findMember(member.getMemberId())).willReturn(member);

        // when
        MemberResponseDto memberResponseDto = memberService.findMemberByMemberId(member.getMemberId());

        // then
        assertThat(memberResponseDto.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(memberResponseDto.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 회원 조회")
    void findUnregisteredMember() {
        // given
        // 임의의 존재하지 않는 회원 번호를 조회, 레포에서 null 반환
        given(memberRepository.findMember(any(Long.class))).willReturn(null);

        // when
        // 회원을 찾지 못한 경우 null 반환
        MemberResponseDto memberResponseDto = memberService.findMemberByMemberId(-1L);

        // then
        assertThat(memberResponseDto).isNull();
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMember() {
        // given
        Member member = new Member();
        member.setMemberId(99L);
        member.setLoginId("testId99");
        member.setPassword("testPw99");
        member.setEmail("test99@gmail.com");

        given(memberRepository.findMember(any(Long.class))).willReturn(member);
        given(memberRepository.deleteMember(any(Long.class))).willReturn(member);

        // when
        // 삭제된 회원 정보를 담은 DTO 반환
        MemberResponseDto memberResponseDto = memberService.deleteMember(member.getMemberId());

        // then
        assertThat(memberResponseDto.getLoginId()).isEqualTo(member.getLoginId());
        assertThat(memberResponseDto.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 회원 삭제")
    void deleteUnregisteredMember() {
        // given
        given(memberRepository.findMember(any(Long.class))).willReturn(null);

        // when
        MemberResponseDto memberResponseDto = memberService.deleteMember(-1L);

        // then
        assertThat(memberResponseDto).isNull();
    }
}
