package burgermap.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateDto {
    @Size(min = 8, max = 12)
    private String password;
    @Email
    private String email;
}
