package burgermap.service;

import burgermap.dto.member.MemberJoinDto;
import burgermap.dto.member.MemberResponseDto;

public interface MemberService {
    public MemberResponseDto addMember(MemberJoinDto memberJoinDto);
    public MemberResponseDto findMemberByMemberId(Long memberId);
    public MemberResponseDto deleteMember(Long memberId);
}
