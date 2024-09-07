package burgermap.dto.member;

import burgermap.enums.MemberType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberJoinRequestDto {
    private MemberType memberType;
    private String loginId;
    private String password;
    private String email;
    private String nickname;
}
