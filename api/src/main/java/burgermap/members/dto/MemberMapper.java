package burgermap.members.dto;

import burgermap.members.Member;

public class MemberMapper {

    public static void member2MemberResponseDTO(Member member, MemberResponseDTO dto) {
        dto.setLoginId(member.getLoginId());
        dto.setEmail(member.getEmail());
    }

    public static void memberJoinDTO2Member(MemberJoinDTO dto, Member member) {
        member.setLoginId(dto.getLoginId());
        member.setPassword(dto.getPassword());
        member.setEmail(dto.getEmail());
    }
}
