package burgermap.repository;

import burgermap.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MySqlMemberRepository implements MemberRepository {

    private final SpringDataJpaMemberRepository repository;

    @Override
    public Member save(Member member) {
        repository.save(member);
        return member;
    }

    @Override
    public Optional<Member> findByMemberId(Long memberId) {
        return repository.findById(memberId);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return repository.findByLoginId(loginId);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return repository.findByNickname(nickname);
    }

    @Override
    public Optional<Member> deleteByMemberId(Long memberId) {
        Member member = repository.findById(memberId).orElseThrow();
        repository.deleteById(memberId);
        return Optional.of(member);
    }
}
