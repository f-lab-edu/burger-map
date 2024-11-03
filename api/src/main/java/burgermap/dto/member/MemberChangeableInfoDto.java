package burgermap.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberChangeableInfoDto {
    private String password;
    private String email;
    private String nickname;
    private String profileImageName;
}
