package burgermap.controller;

import burgermap.annotation.CheckLogin;
import burgermap.dto.image.ImageUploadRequestDto;
import burgermap.dto.image.ImageUploadUrlDto;
import burgermap.dto.member.MemberChangeableInfoDto;
import burgermap.dto.member.MemberInfoDto;
import burgermap.dto.member.MemberJoinRequestDto;
import burgermap.dto.member.MemberJoinResponseDto;
import burgermap.dto.member.MemberLoginDto;
import burgermap.entity.Member;
import burgermap.service.ImageService;
import burgermap.service.MemberService;
import burgermap.session.SessionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.Map;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("members")
public class MemberController {
    private static final String IMAGE_DIRECTORY_PATH = "profile-images";

    private final MemberService memberService;
    private final ImageService imageService;


    /**
     * 회원 추가
     */
    @PostMapping
    public MemberJoinResponseDto addMember(@RequestBody MemberJoinRequestDto memberJoinRequestDto) {
        // 새로운 이미지 파일명 생성 및 이미지 업로드 URL 생성
        Optional<ImageUploadUrlDto> presignedUploadUrl = imageService.createPresignedUploadUrl(
                IMAGE_DIRECTORY_PATH,
                memberJoinRequestDto.getProfileImageName());
        Member member = cvtToMember(
                memberJoinRequestDto,
                presignedUploadUrl.map(ImageUploadUrlDto::getImageName).orElse(null));
        memberService.addMember(member);
        return cvtToMemberJoinResponseDto(
                member,
                presignedUploadUrl.map(ImageUploadUrlDto::getImageUploadUrl).orElse(null));
    }

    @PostMapping("/image-upload-url")
    public ImageUploadUrlDto getImageUploadUrl(@RequestBody ImageUploadRequestDto imageUploadRequestDto) {
        Optional<ImageUploadUrlDto> presignedUploadUrl = imageService.createPresignedUploadUrl(
                IMAGE_DIRECTORY_PATH,
                imageUploadRequestDto.getImageName());
        return presignedUploadUrl.orElseThrow();
    }

    /**
     * 아이디 중복 체크
     */
    @GetMapping("/check-id/{loginId}")
    public Map<String, Boolean> checkId(@PathVariable String loginId) {
        boolean isDuplicated = memberService.checkIdDuplication(loginId);
        return Map.of("available", !isDuplicated);
    }

    /**
     * 이메일 중복 체크
     */
    @GetMapping("/check-email/{email}")
    public Map<String, Boolean> checkEmail(@PathVariable String email) {
        boolean isDuplicated = memberService.checkEmailDuplication(email);
        return Map.of("available", !isDuplicated);
    }

    /**
     * 닉네임 중복 체크
     */
    @GetMapping("/check-nickname/{nickname}")
    public Map<String, Boolean> checkNickname(@PathVariable String nickname) {
        boolean isDuplicated = memberService.checkNicknameDuplication(nickname);
        return Map.of("available", !isDuplicated);
    }

    /**
     * 로그인
     * 세션에 회원 식별 아이디 저장
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Boolean>> login(@RequestBody MemberLoginDto memberLoginDto, HttpServletRequest request) {
        Member member = memberService.login(memberLoginDto.getLoginId(), memberLoginDto.getPassword());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("login", false));
        }
        request.getSession().setAttribute(SessionConstants.loginMember, member.getMemberId());
        return ResponseEntity.ok(Map.of("login", true));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * 내 정보 조회
     * 로그인한 회원이 자신의 정보를 조회
     *  목적: 정보 수정을 위한 기존 정보 조회
     */
    @CheckLogin
    @GetMapping("/my-info")
    public ResponseEntity<MemberInfoDto> getMyInfo(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId) {
        Member member = memberService.getMyInfo(memberId);
        MemberInfoDto memberInfoDto = cvtToMemberInfoDto(member);
        return ResponseEntity.ok(memberInfoDto);
    }

    /**
     * 비밀번호 변경
     * 로그인한 회원이 자신의 비밀번호를 변경
     */
    @CheckLogin
    @PatchMapping("/my-info/password")
    public ResponseEntity<MemberInfoDto> changePassword(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId, @RequestBody MemberChangeableInfoDto memberChangeableInfoDto) {
        Member member = memberService.changePassword(memberId, memberChangeableInfoDto.getPassword());
        return ResponseEntity.ok(cvtToMemberInfoDto(member));
    }

    /**
     * 이메일 변경
     * 로그인한 회원이 자신의 이메일을 변경
     */
    @CheckLogin
    @PatchMapping("/my-info/email")
    public ResponseEntity<MemberInfoDto> changeEmail(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId, @RequestBody MemberChangeableInfoDto memberChangeableInfoDto) {
        Member member = memberService.changeEmail(memberId, memberChangeableInfoDto.getEmail());
        return ResponseEntity.ok(cvtToMemberInfoDto(member));
    }

    /**
     * 닉네임 변경
     * 로그인한 회원이 자신의 닉네임을 변경
     */
    @CheckLogin
    @PatchMapping("/my-info/nickname")
    public ResponseEntity<MemberInfoDto> changeNickname(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId, @RequestBody MemberChangeableInfoDto memberChangeableInfoDto) {
        Member member = memberService.changeNickname(memberId, memberChangeableInfoDto.getNickname());
        return ResponseEntity.ok(cvtToMemberInfoDto(member));
    }

    /**
     * 회원 탈퇴
     * 로그인한 회원이 자신의 회원 정보를 삭제
     * 회원 삭제와 동시에 로그아웃 수행
     */
    @CheckLogin
    @DeleteMapping("/my-info")
    public ResponseEntity<MemberInfoDto> deleteMember(@SessionAttribute(name = SessionConstants.loginMember, required = false) Long memberId, HttpServletRequest request) {
        Member deletedMember = memberService.deleteMember(memberId);
        request.getSession(false).invalidate();  // 삭제와 동시에 로그아웃

        return ResponseEntity.ok(cvtToMemberInfoDto(deletedMember));
    }

    private Member cvtToMember(Object memberDto, String profileImageName) {
        Member member = new Member();

        if (memberDto instanceof MemberJoinRequestDto memberJoinRequestDto) {
            member.setMemberType(memberJoinRequestDto.getMemberType());
            member.setLoginId(memberJoinRequestDto.getLoginId());
            member.setPassword(memberJoinRequestDto.getPassword());
            member.setEmail(memberJoinRequestDto.getEmail());
            member.setNickname(memberJoinRequestDto.getNickname());
            member.setProfileImageName(profileImageName);
        }

        return member;
    }

    private MemberInfoDto cvtToMemberInfoDto(Member member) {
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setMemberType(member.getMemberType());
        memberInfoDto.setLoginId(member.getLoginId());
        memberInfoDto.setEmail(member.getEmail());
        memberInfoDto.setNickname(member.getNickname());
        String profileImageUrl = imageService.getImageUrl(
                IMAGE_DIRECTORY_PATH, member.getProfileImageName()).orElse(null);
        memberInfoDto.setProfileImageUrl(profileImageUrl);
        return memberInfoDto;
    }

    private MemberJoinResponseDto cvtToMemberJoinResponseDto(Member member, String profileImageUploadUrl) {
        MemberJoinResponseDto memberJoinResponseDto = new MemberJoinResponseDto();
        memberJoinResponseDto.setMemberType(member.getMemberType());
        memberJoinResponseDto.setLoginId(member.getLoginId());
        memberJoinResponseDto.setEmail(member.getEmail());
        memberJoinResponseDto.setNickname(member.getNickname());
        memberJoinResponseDto.setProfileImageUploadUrl(profileImageUploadUrl);
        return memberJoinResponseDto;
    }
}
