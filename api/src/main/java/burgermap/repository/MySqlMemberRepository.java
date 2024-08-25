package burgermap.repository;

import burgermap.dto.member.MemberUpdateDto;
import burgermap.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class MySqlMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public Member addMember(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Member findMember(Long memberId) {
        Member member = em.find(Member.class, memberId);
        return member;
    }

    @Override
    public List<Member> findAllMembers() {
        List<Member> memberList = em.createQuery("select a from Member a", Member.class).getResultList();
        return memberList;
    }

    @Override
    public Member deleteMember(Long memberId) {
        Member member = em.find(Member.class, memberId);
        if(member != null) {
            em.remove(member);
        }
        return member;
    }

    @Override
    public Member updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = findMember(memberId);
        if(member != null) {
            if (memberUpdateDto.getPassword() != null) {
                member.setPassword(memberUpdateDto.getPassword());
            }
            if (memberUpdateDto.getEmail() != null) {
                member.setEmail(memberUpdateDto.getEmail());
            }
            em.flush();
        }
        return member;
    }

    @Override
    public void clear() {

    }
}
