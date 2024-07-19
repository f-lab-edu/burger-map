package burgermap.members.repository;

import burgermap.members.Member;

import java.util.List;

public interface MemberRepository {
    public Member addMember(Member member);

    public Member findMember(Long memberId);

    public List<Member> findAllMembers();

    public Member deleteMember(Long memberId);

    public void clear();
}
