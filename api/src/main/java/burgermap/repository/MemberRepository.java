package burgermap.repository;

import burgermap.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    public Member save(Member member);
    public Optional<Member> findByMemberId(Long memberId);
    public Optional<Member> findByLoginId(String loginId);
    public Optional<Member> findByEmail(String email);
    public Optional<Member> findByNickname(String nickname);
    public Optional<Member> deleteByMemberId(Long memberId);
}
