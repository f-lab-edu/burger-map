package burgermap.service;

import burgermap.entity.Image;
import burgermap.entity.Member;
import burgermap.exception.member.MemberNotExistException;
import burgermap.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberLookupService memberLookupService;

    private final MemberRepository repository;

    public void addMember(Member member, String profileImageName) {
        if (profileImageName != null) {
            Image profileImage = new Image();
            profileImage.setImageName(profileImageName);
            member.setProfileImage(profileImage);
        }
        repository.save(member);
        log.debug("member added: {}", member);
    }

    public boolean checkIdDuplication(String loginId) {
        Optional<Member> memberPreoccupying = repository.findByLoginId(loginId);
        boolean isDuplicated = memberPreoccupying.isPresent();
        log.debug("id duplication check: {}", isDuplicated);
        log.debug("preoccupying member: {}", memberPreoccupying.orElse(null));
        return isDuplicated;
    }

    public boolean checkEmailDuplication(String email) {
        Optional<Member> memberPreoccupying = repository.findByEmail(email);
        boolean isDuplicated = memberPreoccupying.isPresent();
        log.debug("email duplication check: {}", isDuplicated);
        log.debug("preoccupying member: {}", memberPreoccupying.orElse(null));
        return isDuplicated;
    }

    public boolean checkNicknameDuplication(String nickname) {
        Optional<Member> memberPreoccupying = repository.findByNickname(nickname);
        boolean isDuplicated = memberPreoccupying.isPresent();
        log.debug("nickname duplication check: {}", isDuplicated);
        log.debug("preoccupying member: {}", memberPreoccupying.orElse(null));
        return isDuplicated;
    }

    public Member login(String loginId, String password) {
        // 로그인 실패: loginId를 가진 회원이 존재하지 않는 경우, 회원은 존재하되 pw가 다른 경우
        Optional<Member> member = repository.findByLoginId(loginId);
        return member.or(() -> { // 아이디에 해당하는 회원이 존재하지 않는 경우
            log.debug("login failed: member with login id {} not exist", loginId);
            return Optional.empty();
        }).map(m -> {  // 비밀번호 비교
            if (!m.getPassword().equals(password)) {
                log.debug("login failed: incorrect password (login id: {})", loginId);
                return null;
            }
            log.debug("login success: {}", m);
            return m;
        }).orElse(null);
    }

    public Member getMyInfo(Long memberId) {
        Member member = memberLookupService.findByMemberId(memberId);
        log.debug("member info: {}", member);
        return member;
    }

    public Member changePassword(Long memberId, String newPassword) {
        Member member = memberLookupService.findByMemberId(memberId);
        member.setPassword(newPassword);
        log.debug("member password changed: {}", member);
        return member;
    }

    public Member changeEmail(Long memberId, String newEmail) {
        Member member = memberLookupService.findByMemberId(memberId);
        member.setEmail(newEmail);
        log.debug("member email changed: {}", member);
        return member;
    }

    public Member changeNickname(Long memberId, String newNickname) {
        Member member = memberLookupService.findByMemberId(memberId);
        member.setNickname(newNickname);
        log.debug("member nickname changed: {}", member);
        return member;
    }

    public Member changeProfileImage(Long memberId, String profileImageName) {
        Member member = memberLookupService.findByMemberId(memberId);
        if (profileImageName == null) {  // 프로필 이미지 삭제
            member.setProfileImage(null);
        } else {
            if (member.getProfileImage() != null)
                member.getProfileImage().setInUse(false);  // 기존 이미지 미사용 처리
            Image newProfileImage = new Image();  // 새 이미지 등록
            newProfileImage.setImageName(profileImageName);
            member.setProfileImage(newProfileImage);
        }
        return member;
    }

    public Member deleteMember(Long memberId) {
        Member member = repository.deleteByMemberId(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));;
        log.debug("member deleted: {}", member);
        return member;
    }
}
