package burgermap.service;

import burgermap.entity.Image;
import burgermap.entity.Member;
import burgermap.enums.MemberType;
import burgermap.exception.member.MemberNotExistException;
import burgermap.exception.store.NotOwnerMemberException;
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
        Optional<Member> member = repository.findByLoginId(loginId);
        // 로그인 실패: loginId를 가진 회원이 존재하지 않는 경우, 회원은 존재하되 pw가 다른 경우
        if (member.isPresent()) {
            log.debug("login member : {}", member.get());
            if (member.get().getPassword().equals(password)) {
                return member.get();
            } else {
                log.debug("login fail: incorrect password");
                return null;
            }
        }
        log.debug("login fail: member not found (loginId: {})", loginId);
        return null;
    }

    public Member getMyInfo(Long memberId) {
        Member member = repository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));
        log.debug("member info: {}", member);
        return member;
    }

    public Member changePassword(Long memberId, String newPassword) {
        Member member = repository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));
        member.setPassword(newPassword);
        log.debug("member password changed: {}", member);
        return member;
    }

    public Member changeEmail(Long memberId, String newEmail) {
        Member member = repository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));
        member.setEmail(newEmail);
        log.debug("member email changed: {}", member);
        return member;
    }

    public Member changeNickname(Long memberId, String newNickname) {
        Member member = repository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));
        member.setNickname(newNickname);
        log.debug("member nickname changed: {}", member);
        return member;
    }

    public Member deleteMember(Long memberId) {
        Member member = repository.deleteByMemberId(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));;
        log.debug("member deleted: {}", member);
        return member;
    }

    /**
     * memberId에 해당하는 회원이 OWNER가 아니면 NotOwnerMemberException을 발생시킴
     */
    public void isMemberTypeOwner(Long memberId) {
        Member member = repository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }
    }
}
