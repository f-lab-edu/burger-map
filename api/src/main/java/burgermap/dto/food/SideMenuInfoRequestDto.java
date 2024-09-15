package burgermap.dto.food;

import burgermap.enums.BunType;
import burgermap.enums.PattyType;
import burgermap.enums.SideMenuType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SideMenuInfoRequestDto {
    private String name;
    private int price;
    private String description;
    private SideMenuType sideMenuType;
}
