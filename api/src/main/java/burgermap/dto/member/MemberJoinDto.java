package burgermap.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberJoinDto {
    @NotBlank
    @Size(min = 8, max = 12)
    private String loginId;
    @NotBlank
    @Size(min = 8, max = 12)
    private String password;
    @NotBlank
    @Email
    private String email;
}
