package burgermap.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor  // ObjectMapper 적용 목적
@AllArgsConstructor  // 단위 테스트시 더미 데이터 생성 목적
public class Member {
    private Long memberId;
    private String loginId;
    private String password;
    private String email;
}
