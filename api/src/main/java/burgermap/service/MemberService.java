package burgermap.service;

import burgermap.entity.Member;
import burgermap.enums.MemberType;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;

    public void addMember(Member member) {
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
        log.debug("login member : {}", member.orElse(null));
        // 로그인 실패: loginId를 가진 회원이 존재하지 않는 경우, 회원은 존재하되 pw가 다른 경우
        if (member.isEmpty() || !member.get().getPassword().equals(password)) {
            log.debug("login failed");
            return null;
        }
        return member.get();
    }

    public Member getMyInfo(Long memberId) {
        Member member = repository.findByMemberId(memberId).get();
        log.debug("member info: {}", member);
        return member;
    }

    public Member changePassword(Long memberId, String newPassword) {
        Optional<Member> member = repository.updatePassword(memberId, newPassword);
        log.debug("member password changed: {}", member.get());
        return member.get();
    }

    public Member changeEmail(Long memberId, String newEmail) {
        Optional<Member> member = repository.updateEmail(memberId, newEmail);
        log.debug("member email changed: {}", member.get());
        return member.get();
    }

    public Member changeNickname(Long memberId, String newNickname) {
        Optional<Member> member = repository.updateNickname(memberId, newNickname);
        log.debug("member nickname changed: {}", member.get());
        return member.get();
    }

    public Member deleteMember(Long memberId) {
        Optional<Member> member = repository.deleteByMemberId(memberId);
        log.debug("member deleted: {}", member.get());
        return member.get();
    }

    /**
     * memberId에 해당하는 회원이 OWNER가 아니면 NotOwnerMemberException을 발생시킴
     */
    public void isMemberTypeOwner(Long memberId) {
        Member member = repository.findByMemberId(memberId).get();
        if (member.getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }
    }
}
