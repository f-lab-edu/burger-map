package burgermap.service;

import burgermap.entity.Member;
import burgermap.exception.member.MemberNotExistException;
import burgermap.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberLookupService {

    private final MemberRepository repository;

    private Optional<Member> findByMemberIdPrimitive(Long memberId) {
        return repository.findByMemberId(memberId);
    }

    Member findByMemberId(Long memberId) {
        return findByMemberIdPrimitive(memberId)
                .orElseThrow(() -> new MemberNotExistException(memberId));
    }

    @Async
    CompletableFuture<Optional<Member>> findByMemberIdAsync(Long memberId) {
        log.debug("async method call");
        return CompletableFuture.completedFuture(findByMemberIdPrimitive(memberId));
    }
}
