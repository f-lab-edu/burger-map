package burgermap.members.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberJoinDTO {
    private String loginId;
    private String password;
    private String email;
}
