package burgermap.members.repository;

import burgermap.members.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class HashMapMemberRepository implements MemberRepository{

    private final Map<Long, Member> repository = new HashMap<>();
    private long memberIdCount = 0L;

    /**
     * 회원 등록
     * 등록순으로 member Id 부여
     */
    @Override
    public Member addMember(Member member) {
        member.setMemberId(++memberIdCount);
        repository.put(memberIdCount, member);
        return member;
    }

    /**
     * member id를 통한 회원 조회
     */
    @Override
    public Member findMember(Long memberId) {
        return repository.get(memberId);
    }

    /**
     * 모든 회원 조회
     */
    @Override
    public List<Member> findAllMembers() {
        return new ArrayList<>(repository.values());
    }

    public void clear() {
        repository.clear();
    }
}
