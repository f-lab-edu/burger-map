package burgermap.entity;

import burgermap.enums.FoodType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Food {
    private Long foodId;
    private Long storeId;
    private String name;
    private int price;
    private String description;
    private FoodType foodType;
}
