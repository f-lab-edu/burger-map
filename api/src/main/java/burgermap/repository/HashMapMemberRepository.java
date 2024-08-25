package burgermap.repository;

import burgermap.dto.member.MemberUpdateDto;
import burgermap.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

//@Repository
public class HashMapMemberRepository implements MemberRepository{

    private final Map<Long, Member> repository = new ConcurrentHashMap<>();
    private AtomicLong memberIdCount = new AtomicLong(0);

    /**
     * 회원 등록
     * 등록순으로 member Id 부여
     */
    @Override
    public Member addMember(Member member) {
        member.setMemberId(memberIdCount.incrementAndGet());
        repository.put(memberIdCount.get(), member);
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

    @Override
    public Member updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = findMember(memberId);
        if (member != null) {
            if (memberUpdateDto.getPassword() != null) {
                member.setPassword(memberUpdateDto.getPassword());
            }
            if (memberUpdateDto.getEmail() != null) {
                member.setEmail(memberUpdateDto.getEmail());
            }
        }

        return member;
    }

    /**
     * 모든 회원 삭제
     */
    @Override
    public void clear() {
        repository.clear();
        memberIdCount.set(0);
    }

    public void printAllMembers() {
        repository.forEach((k, v) -> System.out.println(v));
    }
}
