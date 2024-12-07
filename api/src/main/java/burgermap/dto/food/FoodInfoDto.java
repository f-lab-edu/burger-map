package burgermap.dto.food;

import burgermap.entity.Ingredient;
import burgermap.entity.MenuCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FoodInfoDto {
    private Long foodId;
    private Long storeId;
    private String name;
    private Integer price;
    private String description;
    private String menuTypeValue;
    private MenuCategory menuCategory;
    private List<Ingredient> ingredients;
}
