package burgermap.service;

import burgermap.entity.Member;
import burgermap.exception.member.MemberNotExistException;
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
}
