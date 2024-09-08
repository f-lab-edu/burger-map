package burgermap.dto.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StoreRequestDto {
    private String name;
    private String address;
    private String phone;
    private String introduction;
}
