package burgermap.mapper;

import burgermap.dto.member.MemberJoinDto;
import burgermap.dto.member.MemberResponseDto;
import burgermap.entity.Member;

public class MemberMapper {

    public static void member2MemberResponseDto(Member member, MemberResponseDto dto) {
        dto.setLoginId(member.getLoginId());
        dto.setEmail(member.getEmail());
    }

    public static void memberJoinDto2Member(MemberJoinDto dto, Member member) {
        member.setLoginId(dto.getLoginId());
        member.setPassword(dto.getPassword());
        member.setEmail(dto.getEmail());
    }
}
