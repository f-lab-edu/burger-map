package burgermap.service;

import burgermap.entity.Member;
import burgermap.enums.MemberType;
import burgermap.exception.member.MemberNotExistException;
import burgermap.exception.store.NotOwnerMemberException;
import burgermap.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLookupService {

    private final MemberRepository repository;

    Member findByMemberId(Long memberId) {
        return repository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));
    }

    /**
     * memberId에 해당하는 회원이 OWNER인지 확인. 아닌 경우 NotOwnerMemberException을 발생시킴
     */
    public void isMemberTypeOwner(Long memberId) {
        if (findByMemberId(memberId).getMemberType() != MemberType.OWNER) {
            throw new NotOwnerMemberException("member type is not OWNER.");
        }
    }
}
