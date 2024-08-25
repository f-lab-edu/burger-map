package burgermap.repository;

import burgermap.dto.member.MemberUpdateDto;
import burgermap.entity.Member;

import java.util.List;

public interface MemberRepository {
    public Member addMember(Member member);

    public Member findMember(Long memberId);

    public List<Member> findAllMembers();

    public Member deleteMember(Long memberId);

    public Member updateMember(Long memberId, MemberUpdateDto memberUpdateDto);

    public void clear();
}
