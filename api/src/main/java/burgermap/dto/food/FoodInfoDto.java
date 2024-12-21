package burgermap.dto.food;

import burgermap.entity.Ingredient;
import burgermap.entity.MenuCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
