package burgermap.entity;

import burgermap.enums.MemberType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor  // ObjectMapper 적용 목적
@AllArgsConstructor  // 단위 테스트시 더미 데이터 생성 목적
public class Member {
    private Long memberId;
    private MemberType memberType;
    private String loginId;
    private String password;
    private String email;
    private String nickname;
}
