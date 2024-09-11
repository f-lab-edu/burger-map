package burgermap.dto.store;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreInfoDto {
    private Long storeId;  // id
    private String name;
    private String address;
    private String phone;
    private String introduction;
}
