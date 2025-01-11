package burgermap.entity;

import burgermap.enums.MemberType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor  // ObjectMapper 적용 목적
@AllArgsConstructor  // 단위 테스트시 더미 데이터 생성 목적
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private MemberType memberType;
    private String loginId;
    private String password;
    private String email;
    private String nickname;
    @OneToOne(optional = true, cascade = CascadeType.ALL)  // Image 엔티티의 persist 관리가 구현되지 않아 CascadeType.ALL 추가
    private Image profileImage;

    @Builder
    public Member(MemberType memberType, String loginId, String password, String email, String nickname) {
        this.memberType = memberType;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }
}
