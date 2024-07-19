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

    /**
     * 회원 삭제
     */
    @Override
    public Member deleteMember(Long memberId) {
        Member removedMember = repository.remove(memberId);
        return removedMember;
    }

    /**
     * 로그인 아이디 중복 확인
     */
    public boolean checkLoginIdDuplication(String loginId) {
        return repository.values()
                .stream()
                .anyMatch(member -> member.getLoginId().equals(loginId));
    }

    /**
     * 모든 회원 삭제
     */
    @Override
    public void clear() {
        repository.clear();
        memberIdCount = 0L;
    }

    public void printAllMembers() {
        repository.forEach((k, v) -> System.out.println(v));
    }
}
