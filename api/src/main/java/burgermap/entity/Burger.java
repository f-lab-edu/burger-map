package burgermap.entity;

import burgermap.enums.BunType;
import burgermap.enums.FoodType;
import burgermap.enums.PattyType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Burger extends Food {
    private FoodType foodType = FoodType.BURGER;
    private BunType bunType;
    private PattyType pattyType;
}
