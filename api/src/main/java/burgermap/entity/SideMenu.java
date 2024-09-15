package burgermap.entity;

import burgermap.enums.BunType;
import burgermap.enums.FoodType;
import burgermap.enums.PattyType;
import burgermap.enums.SideMenuType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SideMenu extends Food {
    private FoodType foodType = FoodType.SIDE;
    private SideMenuType sideMenuType;
}
