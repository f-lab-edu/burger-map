package burgermap.controller;

import burgermap.dto.member.MemberJoinDto;
import burgermap.dto.member.MemberResponseDto;
import burgermap.dto.member.MemberUpdateDto;
import burgermap.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


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
    public MemberResponseDto addMember(@RequestBody @Validated MemberJoinDto memberJoinDto) {
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

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponseDto> updateMember(
            @PathVariable("memberId") Long memberId,
            @RequestBody @Valid MemberUpdateDto memberUpdateDto,
            BindingResult bindingResult
            ) throws MethodArgumentNotValidException, NoSuchMethodException {
        if (memberUpdateDto.getEmail() == null && memberUpdateDto.getPassword() == null) {
            bindingResult.reject("allFieldsNull", "하나 이상의 필드를 입력해야합니다.");
            throw new MethodArgumentNotValidException(
                    new MethodParameter(
                            this.getClass().getDeclaredMethod("updateMember", Long.class, MemberUpdateDto.class, BindingResult.class),
                            0),
                    bindingResult
            );
        }

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(
                    new MethodParameter(
                            this.getClass().getDeclaredMethod("updateMember", Long.class, MemberUpdateDto.class, BindingResult.class),
                            0),
                    bindingResult
            );
        }

        MemberResponseDto memberResponseDto = memberService.updateMember(memberId, memberUpdateDto);
        if (memberResponseDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(memberResponseDto);
    }
}
