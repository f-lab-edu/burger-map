package burgermap.controller;

import burgermap.dto.member.MemberJoinDto;
import burgermap.dto.member.MemberResponseDto;
import burgermap.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 추가
     */
    @PostMapping
    public MemberResponseDto addMember(@RequestBody MemberJoinDto memberJoinDto) {
        return memberService.addMember(memberJoinDto);
    }

    /**
     * 회원 식별 번호를 통한 회원 조회
     */
    @GetMapping("/{memberId}")
    public MemberResponseDto findMemberByMemberID(@PathVariable("memberId") Long memberId, HttpServletResponse response) {
        MemberResponseDto foundMemberDto = memberService.findMemberByMemberId(memberId);
        if (foundMemberDto == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return foundMemberDto;
    }

    /**
     * 회원 식별 번호를 통한 회원 삭제
     */
    @DeleteMapping("/{memberId}")
    public MemberResponseDto deleteMember(@PathVariable("memberId") Long memberId, HttpServletResponse response) {
        MemberResponseDto deletedMember = memberService.deleteMember(memberId);
        if (deletedMember == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return deletedMember;
    }
}
