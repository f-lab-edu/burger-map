package burgermap.repository;

import burgermap.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
public class HashMapMemberRepository implements MemberRepository{

    private final Map<Long, Member> repository = new ConcurrentHashMap<>();
    private final AtomicLong memberIdCount = new AtomicLong(0);

    @Override
    public Member save(Member member) {
        member.setMemberId(memberIdCount.incrementAndGet());
        repository.put(member.getMemberId(), member);
        return member;
    }

    @Override
    public Optional<Member> findByMemberId(Long memberId) {
        return Optional.ofNullable(repository.get(memberId));
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return repository.values().stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findAny();
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return repository.values().stream()
                .filter(member -> member.getEmail().equals(email))
                .findAny();
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return repository.values().stream()
                .filter(member -> member.getNickname().equals(nickname))
                .findAny();
    }

    @Override
    public Optional<Member> updatePassword(Long memberId, String newPassword) {
        Member member = repository.get(memberId);
        if (member == null) {
            return Optional.empty();
        }
        member.setPassword(newPassword);
        return Optional.of(member);
    }

    @Override
    public Optional<Member> updateEmail(Long memberId, String newEmail) {
        Member member = repository.get(memberId);
        if (member == null) {
            return Optional.empty();
        }
        member.setEmail(newEmail);
        return Optional.of(member);
    }

    @Override
    public Optional<Member> updateNickname(Long memberId, String newNickname) {
        Member member = repository.get(memberId);
        if (member == null) {
            return Optional.empty();
        }
        member.setNickname(newNickname);
        return Optional.of(member);
    }

    @Override
    public Optional<Member> deleteByMemberId(Long memberId) {
        return Optional.ofNullable(repository.remove(memberId));
    }
}
