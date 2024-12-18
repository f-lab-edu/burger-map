package burgermap.mapper.composite;

import burgermap.dto.member.MemberInfoDto;
import burgermap.dto.member.MemberJoinRequestDto;
import burgermap.dto.member.MemberJoinResponseDto;
import burgermap.entity.Member;
import burgermap.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static burgermap.constants.MemberConstants.IMAGE_DIRECTORY_PATH;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    private final ImageService imageService;

    public Member fromDto(MemberJoinRequestDto memberJoinRequestDto) {
        return Member.builder()
                .memberType(memberJoinRequestDto.getMemberType())
                .loginId(memberJoinRequestDto.getLoginId())
                .password(memberJoinRequestDto.getPassword())
                .email(memberJoinRequestDto.getEmail())
                .nickname(memberJoinRequestDto.getNickname())
                .build();
    }

   public MemberInfoDto toMemberInfoDto(Member member) {
        return MemberInfoDto.builder()
                .memberType(member.getMemberType())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImage() == null ? null : imageService.getImageUrl(IMAGE_DIRECTORY_PATH, member.getProfileImage().getImageName())
                        .orElse(null))
                .build();
   }

   public MemberJoinResponseDto toMemberJoinResponseDto(Member member) {
        return MemberJoinResponseDto.builder()
                .memberType(member.getMemberType())
                .loginId(member.getLoginId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImage() == null ? null : imageService.getImageUrl(IMAGE_DIRECTORY_PATH, member.getProfileImage().getImageName())
                        .orElse(null))
                .build();
   }
}
