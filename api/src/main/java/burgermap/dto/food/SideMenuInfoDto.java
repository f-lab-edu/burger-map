package burgermap.dto.food;

import burgermap.enums.FoodType;
import burgermap.enums.SideMenuType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SideMenuInfoDto {
    private Long foodId;
    private Long storeId;
    private String name;
    private int price;
    private String description;
    private SideMenuType sideMenuType;
    private FoodType foodType;
}
