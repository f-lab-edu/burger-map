package burgermap.dto.member;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class MemberResponseDto {
    private String loginId;
    private String email;
}
